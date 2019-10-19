package com.yunjian.ak.gateway.controller;

import com.alibaba.fastjson.JSON;
import com.yunjian.ak.gateway.vo.ApiAnalysisVo;
import com.yunjian.ak.gateway.vo.ApiPackageVo;
import com.yunjian.ak.web.utils.http.RestUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
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

        RestTemplate template = RestUtils.createRestTemplate();
        ResponseEntity<Map> responseEntity = template.getForEntity(apiAnalysisVo.getJsonUrl(), Map.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            Map data = responseEntity.getBody();
            apiPackageVo.setJsonText(JSON.toJSONString(data));
        }
        return apiPackageVo;
    }
}
