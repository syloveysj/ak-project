package com.yunjian.ak.gateway.controller.apis;

import com.yunjian.ak.gateway.entity.apis.SwaggerEntity;
import com.yunjian.ak.gateway.service.apis.SwaggerService;
import com.yunjian.ak.web.aspect.Log;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/22 0:36
 * @Version 1.0
 */
@RestController
@RequestMapping("/v1/mgr/gateway/swagger")
public class SwaggerController {

    @Autowired
    private SwaggerService swaggerService;

    @PostMapping
    @ApiOperation("插入Swagger")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "添加Swagger成功",
            response = SwaggerEntity.class
    )})
    @Log(value="插入Swagger")
    public SwaggerEntity insert(@Valid @RequestBody SwaggerEntity entity) {
        return this.swaggerService.insert(entity);
    }

    @PutMapping("/{id}")
    @ApiOperation("更新Swagger")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "更新Swagger成功",
            response = SwaggerEntity.class
    )})
    @Log(value="更新Swagger")
    public SwaggerEntity update(@Valid @RequestBody SwaggerEntity entity) {
        return this.swaggerService.update(entity);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除指定id的Swagger")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "删除指定id的Swagger成功"
    )})
    @Log(value="删除指定id的Swagger")
    public void delete(@PathVariable("id") String id) {
        this.swaggerService.delete(id);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取指定id的Swagger")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "获取指定id的Swagger成功",
            response = SwaggerEntity.class
    )})
    @Log(value="获取指定id的Swagger")
    public SwaggerEntity get(@PathVariable("id") String id) {
        return this.swaggerService.get(id);
    }
}
