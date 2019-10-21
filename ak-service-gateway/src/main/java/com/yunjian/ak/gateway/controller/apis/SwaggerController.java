package com.yunjian.ak.gateway.controller.apis;

import com.yunjian.ak.gateway.entity.apis.SwaggerEntity;
import com.yunjian.ak.gateway.service.apis.SwaggerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerController.class);

    @Autowired
    private SwaggerService swaggerService;

    @PostMapping
    @ApiOperation("插入Swagger")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "添加Swagger成功",
            response = SwaggerEntity.class
    )})
    public SwaggerEntity insert(@Valid @RequestBody SwaggerEntity entity) {
        LOGGER.debug("请求SwaggerController的 insert!");

        return this.swaggerService.insert(entity);
    }

    @PutMapping("/{id}")
    @ApiOperation("更新Swagger")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "更新Swagger成功",
            response = SwaggerEntity.class
    )})
    public SwaggerEntity update(@Valid @RequestBody SwaggerEntity entity) {
        LOGGER.debug("请求 SwaggerController 的 update!");

        return this.swaggerService.update(entity);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除指定id的Swagger")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "删除指定id的Swagger成功"
    )})
    public void delete(@PathVariable("id") String id) {
        LOGGER.debug("请求SwaggerController删除指定id的Swagger:{}!", id);

        this.swaggerService.delete(id);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取指定id的Swagger")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "获取指定id的Swagger成功",
            response = SwaggerEntity.class
    )})
    public SwaggerEntity get(@PathVariable("id") String id) {
        LOGGER.debug("请求SwaggerController获取指定id的Swagger!");

        return this.swaggerService.get(id);
    }
}
