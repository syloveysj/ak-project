package com.yunjian.ak.gateway.controller;

import com.yunjian.ak.dao.mybatis.enhance.Page;
import com.yunjian.ak.gateway.entity.TargetEntity;
import com.yunjian.ak.gateway.entity.UpstreamEntity;
import com.yunjian.ak.gateway.service.UpstreamService;
import com.yunjian.ak.kong.client.exception.KongClientException;
import com.yunjian.ak.kong.client.impl.KongClient;
import com.yunjian.ak.kong.client.model.admin.target.Target;
import com.yunjian.ak.kong.client.model.admin.target.TargetList;
import com.yunjian.ak.kong.client.model.admin.upstream.Upstream;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/3 23:07
 * @Version 1.0
 */
@RestController
@RequestMapping("/v1/mgr/gateway/upstreams")
public class UpstreamController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpstreamController.class);

    @Autowired
    private UpstreamService upstreamService;

    @Autowired
    private KongClient kongClient;

    @PostMapping
    @ApiOperation("添加Upstream")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "添加Upstream成功",
            response = UpstreamEntity.class
    )})
    public UpstreamEntity insert(@Valid @RequestBody UpstreamEntity entity) {
        LOGGER.debug("请求 UpstreamController 的 insert!");

        // 调用接口添加上游
        Upstream request = new Upstream();
        BeanUtils.copyProperties(entity, request);
        Upstream result = kongClient.getUpstreamService().createUpstream(request);

        // 调用接口添加目标
        if(entity.getTargets() != null && !entity.getTargets().isEmpty()){
            for (TargetEntity targetEntity : entity.getTargets()) {
                Target target = new Target();
                BeanUtils.copyProperties(targetEntity, target);
                kongClient.getTargetService().createTarget(result.getId(), target);
            }
        }

        // 通过数据库更新别名
        UpstreamEntity data = new UpstreamEntity();
        data.setId(result.getId());
        data.setAlias(entity.getAlias());
        this.upstreamService.update(data);

        return data;
    }

    @PutMapping("/{id}")
    @ApiOperation("更新Upstream")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "更新Upstream成功",
            response = UpstreamEntity.class
    )})
    public UpstreamEntity update(@Valid @RequestBody UpstreamEntity entity) {
        LOGGER.debug("请求 UpstreamController 的 update!");

        return this.upstreamService.update(entity);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除指定id的Upstream")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "删除指定id的Upstream成功"
    )})
    public void delete(@PathVariable("id") String id) {
        LOGGER.debug("请求UpstreamController删除指定id的Upstream:{}!", id);

        // 调用接口删除上游
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

    @GetMapping("/{id}/targets")
    @ApiOperation("获取Upstream的Target列表")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "获取Upstream的Target列表成功",
            response = Target.class,
            responseContainer = "List"
    )})
    public List<Target> getTargets(@PathVariable("id") String id) {
        LOGGER.debug("请求UpstreamController获取Upstream的Target列表!");

        // 调用接口获取所有目标
        try {
            TargetList targetList = kongClient.getTargetService().listTargets(id, null, null, null, 100L, null);
            return targetList.getData();
        } catch (KongClientException e) {
            e.printStackTrace();
        }

        return null;
    }

    @PostMapping("/{id}/targets")
    @ApiOperation("添加Target")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "添加Target成功",
            response = Target.class
    )})
    public Target insertTargets(@PathVariable("id") String id, @Valid @RequestBody TargetEntity entity) {
        LOGGER.debug("请求 UpstreamController 的 Target insert!");

        // 调用接口添加目标
        Target request = new Target();
        BeanUtils.copyProperties(entity, request);
        Target result = kongClient.getTargetService().createTarget(id, request);

        return result;
    }

    @DeleteMapping("/{id}/targets/{tid}")
    @ApiOperation("删除指定id的Target")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "删除指定id的Target成功"
    )})
    public void deleteTargets(@PathVariable("id") String id, @PathVariable("tid") String tid) {
        LOGGER.debug("请求UpstreamController删除指定id的Target:{}!", tid);

        // 调用接口删除上游
        kongClient.getTargetService().deleteTarget(id, tid);
    }

    @DeleteMapping("/{id}/targets")
    @ApiOperation("删除指定id的Target")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "删除指定id的Target成功"
    )})
    public void deleteTargetsList(@PathVariable("id") String id, @RequestParam String ids) {
        LOGGER.debug("请求UpstreamController删除指定ids的Target!");

        if(StringUtils.isEmpty(ids)) return;

        String idList[] = ids.split(",");
        // 调用接口删除上游
        for(String tid : idList) {
            kongClient.getTargetService().deleteTarget(id, tid);
        }
    }
}
