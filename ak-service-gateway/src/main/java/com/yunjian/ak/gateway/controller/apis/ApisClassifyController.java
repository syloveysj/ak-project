package com.yunjian.ak.gateway.controller.apis;

import com.yunjian.ak.gateway.entity.apis.ApisClassifyEntity;
import com.yunjian.ak.gateway.service.apis.ApisClassifyService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/22 0:35
 * @Version 1.0
 */
@RestController
@RequestMapping("/v1/mgr/gateway/apis-classify")
public class ApisClassifyController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApisClassifyController.class);

    @Autowired
    private ApisClassifyService apisClassifyService;

    @PostMapping
    @ApiOperation("插入ApisClassify")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "添加ApisClassify成功",
            response = ApisClassifyEntity.class
    )})
    public ApisClassifyEntity insert(@Valid @RequestBody ApisClassifyEntity entity) {
        LOGGER.debug("请求ApisClassifyController的 insert!");

        return this.apisClassifyService.insert(entity);
    }

    @PutMapping("/{id}")
    @ApiOperation("更新ApisClassify")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "更新ApisClassify成功",
            response = ApisClassifyEntity.class
    )})
    public ApisClassifyEntity update(@Valid @RequestBody ApisClassifyEntity entity) {
        LOGGER.debug("请求 ApisClassifyController 的 update!");

        return this.apisClassifyService.update(entity);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除指定id的ApisClassify")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "删除指定id的ApisClassify成功"
    )})
    public void delete(@PathVariable("id") String id) {
        LOGGER.debug("请求ApisClassifyController删除指定id的ApisClassify:{}!", id);

        this.apisClassifyService.delete(id);
    }

    @GetMapping
    @ApiOperation("获取所有ApisClassify")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "获取所有ApisClassify成功",
            response = ApisClassifyEntity.class,
            responseContainer = "List"
    )})
    public List<ApisClassifyEntity> getLis(@RequestParam(value = "cond", required = false) String cond) {
        LOGGER.debug("请求ApisClassifyController获取所有ApisClassify!");

        return this.apisClassifyService.getList(cond);
    }

    @GetMapping("/services/{serviceId}")
    @ApiOperation("获取所有ApisClassify")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "获取所有ApisClassify成功",
            response = ApisClassifyEntity.class,
            responseContainer = "List"
    )})
    public List<ApisClassifyEntity> getListByServiceId(@PathVariable("serviceId") String serviceId) {
        LOGGER.debug("请求ApisClassifyController获取所有ApisClassify!");

        return this.apisClassifyService.getListByServiceId(serviceId);
    }
}
