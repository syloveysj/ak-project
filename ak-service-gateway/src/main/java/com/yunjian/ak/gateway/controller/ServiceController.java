package com.yunjian.ak.gateway.controller;

import com.yunjian.ak.gateway.entity.ServiceEntity;
import com.yunjian.ak.gateway.service.ServiceService;
import com.yunjian.ak.kong.client.impl.KongClient;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/3 23:11
 * @Version 1.0
 */
@RestController
@RequestMapping("/v1/mgr/gateway/services")
public class ServiceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpstreamController.class);

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private KongClient kongClient;

    @DeleteMapping("/{id}")
    @ApiOperation("删除指定id的Service")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "删除指定id的Service成功"
    )})
    public void delete(@PathVariable("id") String id) {
        LOGGER.debug("请求ServiceController删除指定id的Service:{}!", id);

        // 调用接口删除服务
        kongClient.getServiceService().deleteService(id);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取指定id的Service")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "获取指定id的Service成功",
            response = ServiceEntity.class
    )})
    public ServiceEntity get(@PathVariable("id") String id) {
        LOGGER.debug("请求UpstreamController获取指定id的Service!");

        return this.serviceService.get(id);
    }
}
