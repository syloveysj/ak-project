package com.yunjian.ak.gateway.controller.apis;

import com.alibaba.fastjson.JSON;
import com.yunjian.ak.dao.mybatis.enhance.Page;
import com.yunjian.ak.exception.ValidationException;
import com.yunjian.ak.gateway.controller.router.UpstreamController;
import com.yunjian.ak.gateway.entity.apis.RouteEntity;
import com.yunjian.ak.gateway.entity.apis.ServiceEntity;
import com.yunjian.ak.gateway.entity.apis.TargetEntity;
import com.yunjian.ak.gateway.entity.apis.UpstreamEntity;
import com.yunjian.ak.gateway.service.apis.ApisRouteService;
import com.yunjian.ak.gateway.service.apis.ApisServiceService;
import com.yunjian.ak.gateway.service.apis.ApisUpstreamService;
import com.yunjian.ak.gateway.vo.apis.ApiAnalysisVo;
import com.yunjian.ak.gateway.vo.apis.ApiPackageVo;
import com.yunjian.ak.gateway.vo.apis.ApiVo;
import com.yunjian.ak.kong.client.exception.KongClientException;
import com.yunjian.ak.kong.client.impl.KongClient;
import com.yunjian.ak.kong.client.model.admin.route.Route;
import com.yunjian.ak.kong.client.model.admin.route.RouteList;
import com.yunjian.ak.kong.client.model.admin.service.Service;
import com.yunjian.ak.kong.client.model.admin.target.Target;
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
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.nio.charset.Charset;
import java.util.*;

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
    private ApisRouteService apisRouteService;

    @Autowired
    private KongClient kongApisClient;

    @PostMapping("/services")
    @ApiOperation("添加Services")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "添加Services成功",
            response = ServiceEntity.class
    )})
    public ServiceEntity insertService(@Valid @RequestBody ServiceEntity entity) {
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
        return entity;
    }

    @PutMapping("/services/{id}")
    @ApiOperation("更新Services")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "添加Services成功",
            response = ServiceEntity.class
    )})
    public ServiceEntity updateService(@Valid @RequestBody ServiceEntity entity) {
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
    public void deleteService(@PathVariable("id") String id) {
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

    @GetMapping("/services/{id}")
    @ApiOperation("获取指定id的Services")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "获取指定id的Services成功",
            response = ServiceEntity.class
    )})
    public ServiceEntity getService(@PathVariable("id") String id) {
        LOGGER.debug("请求 ApisController 获取指定id的Services:{}!", id);

        return apisServiceService.get(id);
    }

    @GetMapping("/services")
    @ApiOperation("获取所有Services")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "获取所有Services成功",
            response = ServiceEntity.class,
            responseContainer = "List"
    )})
    public List<ServiceEntity> getAllServices() {
        LOGGER.debug("请求 ApisController 获取所有 Services!");

        return this.apisServiceService.getAll();
    }

    @PostMapping("/services/{upstreamName}/targets")
    @ApiOperation("添加Target")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "添加Target成功",
            response = Target.class
    )})
    public Target insertTarget(@PathVariable("upstreamName") String upstreamName, @Valid @RequestBody TargetEntity entity) {
        LOGGER.debug("请求 ApisController 的 Target insert!");

        // 调用接口添加目标
        Target target = new Target();
        BeanUtils.copyProperties(entity, target);
        target = kongApisClient.getTargetService().createTarget(upstreamName, target);

        return target;
    }

    @DeleteMapping("/services/{upstreamName}/targets/{tid}")
    @ApiOperation("删除指定id的Target")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "删除指定id的Target成功"
    )})
    public void deleteTarget(@PathVariable("upstreamName") String upstreamName, @PathVariable("tid") String tid) {
        LOGGER.debug("请求 ApisController 删除指定id的Target:{}!", tid);

        // 调用接口删除目标
        kongApisClient.getTargetService().deleteTarget(upstreamName, tid);
    }

    @DeleteMapping("/services/{upstreamName}/targets")
    @ApiOperation("删除指定ids的Target")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "删除指定id的Target成功"
    )})
    public void deleteTargets(@PathVariable("upstreamName") String upstreamName, @RequestParam String ids) {
        LOGGER.debug("请求 ApisController 删除指定ids的Target!");

        if(StringUtils.isEmpty(ids)) return;

        String idList[] = ids.split(",");
        // 调用接口删除目标
        for(String tid : idList) {
            kongApisClient.getTargetService().deleteTarget(upstreamName, tid);
        }
    }

    @GetMapping("/services/{upstreamName}/targets")
    @ApiOperation("获取Upstream的Target列表")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "获取Upstream的Target列表成功",
            response = Target.class,
            responseContainer = "List"
    )})
    public List<Target> getTargets(@PathVariable("upstreamName") String upstreamName) {
        LOGGER.debug("请求 ApisController 获取Upstream的Target列表!");

        // 调用接口获取所有目标
        try {
            TargetList targetList = kongApisClient.getTargetService().listTargets(upstreamName, null, null, null, 100L, null);
            return targetList.getData();
        } catch (KongClientException e) {
            e.printStackTrace();
        }

        return null;
    }

    @PostMapping("/routes")
    @ApiOperation("添加Routes")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "添加Routes成功"
    )})
    public void insertRoutes(@Valid @RequestBody ApiPackageVo entity) {
        LOGGER.debug("请求 ApisController 的 Routes insert!");

        // 调用接口添加路由
        Service service = new Service();
        service.setId(entity.getServerId());

        List<RouteEntity> routeEntities = new ArrayList<>();
        for(ApiVo row : entity.getRows()) {
            Route route = new Route();
            route.setService(service);
            route.setName(row.getName());
            route.setMethods(Arrays.asList(row.getMethod()));
            route.setHosts(Arrays.asList());
            route.setPaths(Arrays.asList(row.getUri()));
            route.setProtocols(Arrays.asList("http", "https"));
            route.setStripPath(true);
            route.setPreserveHost(false);
            if(row.getState() == 0) { // 添加
                route = kongApisClient.getRouteService().createRoute(route);
            } else { // 更新
                route = kongApisClient.getRouteService().updateRoute(row.getId(), route);
            }

            RouteEntity routeEntity = new RouteEntity();
            routeEntity.setId(route.getId());
            routeEntity.setAlias(row.getAlias());
            routeEntity.setMemo(row.getMemo());
            routeEntity.setClassifyId("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
            routeEntities.add(routeEntity);
        }

        // 通过数据库更新相关信息
        apisRouteService.updateBatch(routeEntities);
    }

    @PutMapping("/routes/classify/{classifyId}")
    @ApiOperation("更新Routes分类")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "更新Routes分类成功",
            response = Integer.class
    )})
    public int updateRoutesClassify(@PathVariable("classifyId") String classifyId, @Valid @RequestBody List<String> ids) {
        LOGGER.debug("请求 ApisController 的 Routes updateRoutesClassify!");

        // 通过数据库更新相关信息
        return apisRouteService.updateClassifyBatch(classifyId, ids);
    }

    @DeleteMapping("/routes/{id}")
    @ApiOperation("删除指定id的Route")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "删除指定id的Route成功"
    )})
    public void deleteRoute(@PathVariable("id") String id) {
        LOGGER.debug("请求RouteController删除指定id的Route:{}!", id);

        // 调用接口删除路由
        kongApisClient.getRouteService().deleteRoute(id);
    }

    @DeleteMapping("/routes")
    @ApiOperation("删除指定ids的Routes")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "删除指定id的Routes成功"
    )})
    public void deleteRoutes(@RequestParam String ids) {
        LOGGER.debug("请求 ApisController 删除指定ids的Routes!");

        if(StringUtils.isEmpty(ids)) return;

        String idList[] = ids.split(",");
        // 调用接口删除目标
        for(String id : idList) {
            kongApisClient.getRouteService().deleteRoute(id);
        }
    }

    @GetMapping("/routes")
    @ApiOperation("获取匹配Route列表")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "获取Route列表成功",
            response = Page.class
    )})
    public Page<RouteEntity> getRoutesByPage(@RequestParam("page") int page, @RequestParam("pagesize") int pagesize,
                                            @RequestParam(value = "sort", required = false) String sort, @RequestParam(value = "order", required = false) String order,
                                            @RequestParam(value = "cond", required = false) String cond) {
        LOGGER.debug("请求 ApisController 获取匹配Route列表!");

        return apisRouteService.getListByPage(page, pagesize, sort, order, cond);
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
        apiPackageVo.setServerId(apiAnalysisVo.getServerId());
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
                    String uri = pathsEntry.getKey().replaceAll("\\{[^}]*\\}","[^/]*");
                    if(!StringUtils.endsWithIgnoreCase(uri, "/")) uri += "/?$";

                    ApiVo apiVo = new ApiVo();
                    apiVo.setName(Base64Utils.encodeToUrlSafeString((methodsEntry.getKey().toUpperCase() + ":" + uri).getBytes(Charset.forName("UTF-8"))).replaceAll("=", "."));
                    apiVo.setUri(uri);
                    apiVo.setMemo(pathsEntry.getKey());
                    apiVo.setMethod(methodsEntry.getKey().toUpperCase());
                    apiVo.setState(0);

                    Map methodInfo = methodsEntry.getValue();
                    apiVo.setAlias(MapUtils.getString(methodInfo, "summary"));
                    apiVos.add(apiVo);
                }
            }
        }

        apiPackageVo.setRows(apiVos);
        return apiPackageVo;
    }
}
