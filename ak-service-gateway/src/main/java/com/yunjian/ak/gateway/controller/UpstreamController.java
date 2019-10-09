package com.yunjian.ak.gateway.controller;

import com.yunjian.ak.dao.mybatis.enhance.Page;
import com.yunjian.ak.exception.ValidationException;
import com.yunjian.ak.gateway.entity.GatewayHost;
import com.yunjian.ak.gateway.entity.TargetEntity;
import com.yunjian.ak.gateway.entity.UpstreamEntity;
import com.yunjian.ak.gateway.service.UpstreamService;
import com.yunjian.ak.kong.client.impl.KongClient;
import com.yunjian.ak.kong.client.model.admin.target.Target;
import com.yunjian.ak.kong.client.model.admin.target.TargetList;
import com.yunjian.ak.kong.client.model.admin.upstream.Upstream;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/3 23:07
 * @Version 1.0
 */
@RestController
@RequestMapping("/mgr/v1/gateway/upstream")
public class UpstreamController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpstreamController.class);

    @Autowired
    private UpstreamService upstreamService;

    @PostMapping
    @ApiOperation("添加Upstream")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "添加Upstream成功",
            response = UpstreamEntity.class
    )})
    public UpstreamEntity insert(@Valid @RequestBody UpstreamEntity entity) {
        LOGGER.debug("请求 UpstreamController 的 insert!");

        if(StringUtils.isEmpty(entity.getGatewayHost().getUrl())) {
            throw new ValidationException("网关地址不能为空");
        }

        // 调用接口添加上游
        KongClient kongClient = new KongClient(entity.getGatewayHost().getUrl());
        Upstream request = new Upstream();
        BeanUtils.copyProperties(entity, request);
        Upstream result = kongClient.getUpstreamService().createUpstream(request);

        // 调用接口添加目标
        if(entity.getTargets() != null && entity.getTargets().size() > 0){
            for (TargetEntity targetEntity : entity.getTargets()) {
                Target target = new Target();
                BeanUtils.copyProperties(targetEntity, target);
                kongClient.getTargetService().createTarget(result.getId(), target);
            }
        }

        // 通过数据库更新别名
        UpstreamEntity data = new UpstreamEntity();
        BeanUtils.copyProperties(result, data);
        data.setAlias(entity.getAlias());
        this.upstreamService.update(data);

        return data;
    }

    @PostMapping("/{id}")
    @ApiOperation("更新Upstream")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "更新Upstream成功",
            response = UpstreamEntity.class
    )})
    public UpstreamEntity update(@RequestBody UpstreamEntity entity) {
        LOGGER.debug("请求 UpstreamController 的 update!");

        if(StringUtils.isEmpty(entity.getGatewayHost().getUrl())) {
            throw new ValidationException("网关地址不能为空");
        }

        // 调用接口获取所有目标
        KongClient kongClient = new KongClient(entity.getGatewayHost().getUrl());
        TargetList targetList = kongClient.getTargetService().listActiveTargets(entity.getId());

        // 调用接口删除所有目标
        for(Target target : targetList.getData()) {
            kongClient.getTargetService().deleteTarget(entity.getId(), target.getId());
        }

        return this.upstreamService.update(entity);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除指定id的Upstream")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "Upstream"
    )})
    public void delete(@PathVariable("id") String id, @RequestBody GatewayHost entity) {
        LOGGER.debug("请求UpstreamController删除指定id的Upstream:{}!", id);

        if(StringUtils.isEmpty(entity.getUrl())) {
            throw new ValidationException("网关地址不能为空");
        }

        // 调用接口删除上游
        KongClient kongClient = new KongClient(entity.getUrl());
        kongClient.getUpstreamService().deleteUpstream(id);
    }

    @GetMapping
    @ApiOperation("获取匹配Upstream列表")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "获取Upstream列表成功",
            response = Page.class
    )})
    public Page<UpstreamEntity> getListByPage(@RequestParam("page") int page, @RequestParam("pagesize") int pagesize,
                                              @RequestParam(value = "sort", required = false) String sort, @RequestParam(value = "order", required = false) String order,
                                              @RequestParam(value = "cond", required = false) String cond) {
        LOGGER.debug("请求UpstreamController获取匹配Upstream列表!");

        return this.upstreamService.getListByPage(page, pagesize, sort, order, cond);
    }
}
