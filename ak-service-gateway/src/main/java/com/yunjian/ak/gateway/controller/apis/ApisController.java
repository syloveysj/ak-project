package com.yunjian.ak.gateway.controller.apis;

import com.alibaba.fastjson.JSON;
import com.yunjian.ak.exception.ValidationException;
import com.yunjian.ak.gateway.controller.router.UpstreamController;
import com.yunjian.ak.gateway.entity.apis.ServiceEntity;
import com.yunjian.ak.gateway.entity.apis.UpstreamEntity;
import com.yunjian.ak.gateway.service.apis.ApisServiceService;
import com.yunjian.ak.gateway.service.apis.ApisUpstreamService;
import com.yunjian.ak.gateway.vo.apis.ApiAnalysisVo;
import com.yunjian.ak.gateway.vo.apis.ApiPackageVo;
import com.yunjian.ak.gateway.vo.apis.ApiVo;
import com.yunjian.ak.kong.client.impl.KongClient;
import com.yunjian.ak.kong.client.model.admin.route.RouteList;
import com.yunjian.ak.kong.client.model.admin.service.Service;
import com.yunjian.ak.kong.client.model.admin.target.TargetList;
import com.yunjian.ak.kong.client.model.admin.upstream.Upstream;
import com.yunjian.ak.utils.UUIDUtil;
import com.yunjian.ak.web.utils.http.RestUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/19 21:08
 * @Version 1.0
 */
@RestController
@RequestMapping("/v1/mgr/gateway/apis")
public class ApisController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpstreamController.class);

    @Autowired
    private ApisServiceService apisServiceService;

    @Autowired
    private ApisUpstreamService apisUpstreamService;

    @Autowired
    private KongClient kongApisClient;

    @PostMapping("/services")
    @ApiOperation("添加Services")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "添加Services成功",
            response = ServiceEntity.class
    )})
    public ServiceEntity insert(@Valid @RequestBody ServiceEntity entity) {
        LOGGER.debug("请求 ApisController 的 Services insert!");

        // 调用接口添加上游
        Upstream upstream = new Upstream();
        upstream.setName(UUIDUtil.createUUID());
        upstream.setSlots(10000);
        upstream = kongApisClient.getUpstreamService().createUpstream(upstream);

        // 调用接口添加服务
        Service service = new Service();
        service.setName(UUIDUtil.createUUID());
        service.setProtocol("http");
        service.setHost(upstream.getName());
        service.setPort(80);
        service.setConnectTimeout(60000L);
        service.setWriteTimeout(60000L);
        service.setReadTimeout(60000L);
        service = kongApisClient.getServiceService().createService(service);

        // 通过数据库更新相关信息
        UpstreamEntity upstreamEntity = new UpstreamEntity();
        upstreamEntity.setId(upstream.getId());
        upstreamEntity.setAlias(entity.getAlias());
        apisUpstreamService.update(upstreamEntity);

        entity.setId(service.getId());
        apisServiceService.update(entity);
        return null;
    }

    @PutMapping("/services/{id}")
    @ApiOperation("更新Services")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "添加Services成功",
            response = ServiceEntity.class
    )})
    public ServiceEntity update(@Valid @RequestBody ServiceEntity entity) {
        LOGGER.debug("请求 ApisController 的 Services update!");

        // 调用接口添加服务
        Service service = new Service();
        BeanUtils.copyProperties(entity, service);
        service = kongApisClient.getServiceService().updateService(entity.getId(), service);

        // 通过数据库更新相关信息
        UpstreamEntity upstreamEntity = new UpstreamEntity();
        upstreamEntity.setName(entity.getHost());
        upstreamEntity.setAlias(entity.getAlias());
        apisUpstreamService.update(upstreamEntity);

        entity.setId(service.getId());
        apisServiceService.update(entity);
        return entity;
    }

    @DeleteMapping("/services/{id}")
    @ApiOperation("删除指定id的Services")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "删除指定id的Services成功"
    )})
    public void delete(@PathVariable("id") String id) {
        LOGGER.debug("请求 ApisController 删除指定id的Services:{}!", id);

        ServiceEntity entity = apisServiceService.get(id);

        TargetList targetList = kongApisClient.getTargetService().listTargets(entity.getHost(), null, null, null, null, null);
        if(targetList.getData() != null && targetList.getData().size() > 0) {
            throw new ValidationException("请先删除目标Targets");
        }

        RouteList routeList = kongApisClient.getRouteService().listRoutesByService(entity.getId(), null, null, null, 2L, null);
        if(routeList.getData() != null && routeList.getData().size() > 0) {
            throw new ValidationException("请先删除路由Routes");
        }

        kongApisClient.getServiceService().deleteService(entity.getId());
        kongApisClient.getUpstreamService().deleteUpstream(entity.getHost());
    }

    @GetMapping("/services")
    @ApiOperation("获取所有Services")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "获取所有Services成功",
            response = ServiceEntity.class,
            responseContainer = "List"
    )})
    public List<ServiceEntity> getAll() {
        LOGGER.debug("请求ApplicationTypeController获取所有ApplicationType!");

        return this.apisServiceService.getAll();
    }

    @PostMapping("/analysis")
    @ApiOperation("解析服务")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "解析服务成功",
            response = ApiPackageVo.class
    )})
    public ApiPackageVo analysis(@Valid @RequestBody ApiAnalysisVo apiAnalysisVo) {
        System.out.println(apiAnalysisVo);

        ApiPackageVo apiPackageVo = new ApiPackageVo();
        Map data = null;
        if(StringUtils.isNotEmpty(apiAnalysisVo.getJsonUrl())) {
            RestTemplate template = RestUtils.createRestTemplate();
            ResponseEntity<Map> responseEntity = template.getForEntity(apiAnalysisVo.getJsonUrl(), Map.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                data = responseEntity.getBody();
                apiPackageVo.setJsonText(JSON.toJSONString(data));
            }
        } else if(StringUtils.isNotEmpty(apiAnalysisVo.getJsonText())) {
            data = JSON.parseObject(apiAnalysisVo.getJsonText(), Map.class);
            apiPackageVo.setJsonText(apiAnalysisVo.getJsonText());
        }

        if(data == null) {
            throw new ValidationException("json信息无法解析");
        }

        boolean flag = true;

        List<ApiVo> apiVos = new ArrayList<>();
        Map paths = MapUtils.getMap(data, "paths", null);
        if(paths != null) {
            Iterator<Map.Entry<String, Map>> pathsEntries = paths.entrySet().iterator();
            while(pathsEntries.hasNext()){
                Map.Entry<String, Map> pathsEntry = pathsEntries.next();

                Map methods = pathsEntry.getValue();
                Iterator<Map.Entry<String, Map>> methodsEntries = methods.entrySet().iterator();
                while(methodsEntries.hasNext()){
                    Map.Entry<String, Map> methodsEntry = methodsEntries.next();

                    ApiVo apiVo = new ApiVo();
                    apiVo.setUri(pathsEntry.getKey());
                    apiVo.setMethod(methodsEntry.getKey().toUpperCase());
                    apiVo.setState(flag ? 0 : 1);
                    flag = !flag;

                    Map methodInfo = methodsEntry.getValue();
                    apiVo.setName(MapUtils.getString(methodInfo, "summary"));
                    apiVos.add(apiVo);
                }
            }
        }

        apiPackageVo.setRows(apiVos);
        return apiPackageVo;
    }
}
