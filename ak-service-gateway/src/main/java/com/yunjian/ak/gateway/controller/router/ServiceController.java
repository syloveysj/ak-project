package com.yunjian.ak.gateway.controller.router;

import com.yunjian.ak.gateway.entity.router.ServiceEntity;
import com.yunjian.ak.gateway.service.router.ServiceService;
import com.yunjian.ak.kong.client.impl.KongClient;
import com.yunjian.ak.web.aspect.Log;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private KongClient kongRouterClient;

    @DeleteMapping("/{id}")
    @ApiOperation("删除指定id的Service")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "删除指定id的Service成功"
    )})
    @Log(value="删除指定id的Service")
    public void delete(@PathVariable("id") String id) {
        // 调用接口删除服务
        kongRouterClient.getServiceService().deleteService(id);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取指定id的Service")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "获取指定id的Service成功",
            response = ServiceEntity.class
    )})
    @Log(value="获取指定id的Service")
    public ServiceEntity get(@PathVariable("id") String id) {
        return this.serviceService.get(id);
    }
}
