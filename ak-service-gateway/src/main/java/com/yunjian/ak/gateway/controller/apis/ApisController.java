package com.yunjian.ak.gateway.controller.apis;

import com.alibaba.fastjson.JSON;
import com.yunjian.ak.exception.ValidationException;
import com.yunjian.ak.gateway.controller.router.UpstreamController;
import com.yunjian.ak.gateway.entity.apis.ServiceEntity;
import com.yunjian.ak.gateway.service.apis.ApisServiceService;
import com.yunjian.ak.gateway.vo.apis.ApiAnalysisVo;
import com.yunjian.ak.gateway.vo.apis.ApiPackageVo;
import com.yunjian.ak.gateway.vo.apis.ApiVo;
import com.yunjian.ak.web.utils.http.RestUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @GetMapping("/services")
    @ApiOperation("获取所有Services")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "获取所有ApplicationType成功",
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
