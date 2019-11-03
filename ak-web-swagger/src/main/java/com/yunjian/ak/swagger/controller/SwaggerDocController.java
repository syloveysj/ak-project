package com.yunjian.ak.swagger.controller;

import com.yunjian.ak.config.ConfigManager;
import com.yunjian.ak.web.utils.http.RestUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.collections4.MapUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStreamWriter;
import java.util.Map;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/30 23:34
 * @Version 1.0
 */
@RestController
@RequestMapping("/v1/swagger/doc")
public class SwaggerDocController {

    @GetMapping("/{id}")
    @ApiOperation("获取指定id的Swagger文档")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "获取指定id的Swagger文档成功",
            response = String.class
    )})
    public void getDoc(@PathVariable("id") String id, HttpServletResponse response) {
        String result = "{}";
        String url = ConfigManager.getInstance().getConfig("ak_gateway_apis_url") + "/v1/mgr/gateway/swagger/" + id;
        RestTemplate template = RestUtils.createRestTemplate();
        ResponseEntity<Map> responseEntity = template.getForEntity(url, Map.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            Map map = responseEntity.getBody();
            Map data = MapUtils.getMap(map, "data", null);
            result = MapUtils.getString(data, "content", "{}");
            result = result.replace("!!host!!", ConfigManager.getInstance().getConfig("swagger_host"));
        }

        try{
            response.reset();
            OutputStreamWriter ow = new OutputStreamWriter(response.getOutputStream(),"utf-8");
            ow.write(result);
            ow.flush();
            ow.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
