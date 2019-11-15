package com.yunjian.ak.gateway.controller.apis;

import com.yunjian.ak.gateway.entity.apis.ApisClassifyEntity;
import com.yunjian.ak.gateway.service.apis.ApisClassifyService;
import com.yunjian.ak.web.aspect.Log;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @Autowired
    private ApisClassifyService apisClassifyService;

    @PostMapping
    @ApiOperation("插入ApisClassify")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "添加ApisClassify成功",
            response = ApisClassifyEntity.class
    )})
    @Log(value="插入ApisClassify")
    public ApisClassifyEntity insert(@Valid @RequestBody ApisClassifyEntity entity) {
        return this.apisClassifyService.insert(entity);
    }

    @PutMapping("/{id}")
    @ApiOperation("更新ApisClassify")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "更新ApisClassify成功",
            response = ApisClassifyEntity.class
    )})
    @Log(value="更新ApisClassify")
    public ApisClassifyEntity update(@Valid @RequestBody ApisClassifyEntity entity) {
        return this.apisClassifyService.update(entity);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除指定id的ApisClassify")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "删除指定id的ApisClassify成功"
    )})
    @Log(value="删除指定id的ApisClassify")
    public void delete(@PathVariable("id") String id) {
        this.apisClassifyService.delete(id);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取定id的ApisClassify")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "获取定id的ApisClassify成功",
            response = ApisClassifyEntity.class
    )})
    @Log(value="获取定id的ApisClassify")
    public ApisClassifyEntity get(@PathVariable("id") String id) {
        return this.apisClassifyService.get(id);
    }

    @GetMapping
    @ApiOperation("获取所有ApisClassify")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "获取所有ApisClassify成功",
            response = ApisClassifyEntity.class,
            responseContainer = "List"
    )})
    @Log(value="获取所有ApisClassify")
    public List<ApisClassifyEntity> getLis(@RequestParam(value = "cond", required = false) String cond) {
        return this.apisClassifyService.getList(cond);
    }

    @GetMapping("/services/{serviceId}")
    @ApiOperation("获取应用下所有ApisClassify")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "获取所有ApisClassify成功",
            response = ApisClassifyEntity.class,
            responseContainer = "List"
    )})
    @Log(value="获取应用下所有ApisClassify")
    public List<ApisClassifyEntity> getListByServiceId(@PathVariable("serviceId") String serviceId) {
        return this.apisClassifyService.getListByServiceId(serviceId);
    }
}
