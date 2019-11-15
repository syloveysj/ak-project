package com.yunjian.ak.gateway.controller.apis;

import com.yunjian.ak.gateway.entity.apis.ApplicationTypeEntity;
import com.yunjian.ak.gateway.service.apis.ApplicationTypeService;
import com.yunjian.ak.web.aspect.Log;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/22 0:35
 * @Version 1.0
 */
@RestController
@RequestMapping("/v1/mgr/gateway/application-type")
public class ApplicationTypeController {

    @Autowired
    private ApplicationTypeService applicationTypeService;

    @GetMapping("/all")
    @ApiOperation("获取所有ApplicationType")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "获取所有ApplicationType成功",
            response = ApplicationTypeEntity.class,
            responseContainer = "List"
    )})
    @Log(value="获取所有ApplicationType")
    public List<ApplicationTypeEntity> getAll() {
        return this.applicationTypeService.getAll();
    }
}
