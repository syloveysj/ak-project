package com.yunjian.ak.gateway.controller.router;

import com.yunjian.ak.dao.mybatis.enhance.Page;
import com.yunjian.ak.gateway.entity.router.TargetEntity;
import com.yunjian.ak.gateway.entity.router.UpstreamEntity;
import com.yunjian.ak.gateway.service.router.UpstreamService;
import com.yunjian.ak.kong.client.exception.KongClientException;
import com.yunjian.ak.kong.client.impl.KongClient;
import com.yunjian.ak.kong.client.model.admin.target.Target;
import com.yunjian.ak.kong.client.model.admin.target.TargetList;
import com.yunjian.ak.kong.client.model.admin.upstream.Upstream;
import com.yunjian.ak.web.aspect.Log;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/3 23:07
 * @Version 1.0
 */
@RestController
@RequestMapping("/v1/mgr/gateway/upstreams")
public class UpstreamController {

    @Autowired
    private UpstreamService upstreamService;

    @Autowired
    private KongClient kongRouterClient;

    @PostMapping
    @ApiOperation("添加Upstream")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "添加Upstream成功",
            response = UpstreamEntity.class
    )})
    @Log(value="添加Upstream")
    public UpstreamEntity insert(@Valid @RequestBody UpstreamEntity entity) {
        // 调用接口添加上游
        Upstream request = new Upstream();
        BeanUtils.copyProperties(entity, request);
        Upstream result = kongRouterClient.getUpstreamService().createUpstream(request);

        // 调用接口添加目标
        if(entity.getTargets() != null && !entity.getTargets().isEmpty()){
            for (TargetEntity targetEntity : entity.getTargets()) {
                Target target = new Target();
                BeanUtils.copyProperties(targetEntity, target);
                kongRouterClient.getTargetService().createTarget(result.getId(), target);
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
    @Log(value="更新Upstream")
    public UpstreamEntity update(@Valid @RequestBody UpstreamEntity entity) {
        return this.upstreamService.update(entity);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除指定id的Upstream")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "删除指定id的Upstream成功"
    )})
    @Log(value="删除指定id的Upstream")
    public void delete(@PathVariable("id") String id) {
        // 调用接口删除上游
        kongRouterClient.getUpstreamService().deleteUpstream(id);
    }

    @GetMapping("/all")
    @ApiOperation("获取所有Upstream")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "获取所有Upstream成功",
            response = UpstreamEntity.class,
            responseContainer = "List"
    )})
    @Log(value="获取所有Upstream")
    public List<UpstreamEntity> getAll() {
        return this.upstreamService.getAll();
    }

    @GetMapping
    @ApiOperation("获取匹配Upstream列表")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "获取Upstream列表成功",
            response = Page.class
    )})
    @Log(value="获取匹配Upstream列表")
    public Page<UpstreamEntity> getListByPage(@RequestParam("page") int page, @RequestParam("pagesize") int pagesize,
                                              @RequestParam(value = "sort", required = false) String sort, @RequestParam(value = "order", required = false) String order,
                                              @RequestParam(value = "cond", required = false) String cond) {
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
    @Log(value="获取Upstream的Target列表")
    public List<Target> getTargets(@PathVariable("id") String id) {
        // 调用接口获取所有目标
        try {
            TargetList targetList = kongRouterClient.getTargetService().listTargets(id, null, null, null, 100L, null);
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
    @Log(value="添加Target")
    public Target insertTarget(@PathVariable("id") String id, @Valid @RequestBody TargetEntity entity) {
        // 调用接口添加目标
        Target request = new Target();
        BeanUtils.copyProperties(entity, request);
        Target result = kongRouterClient.getTargetService().createTarget(id, request);

        return result;
    }

    @DeleteMapping("/{id}/targets/{tid}")
    @ApiOperation("删除指定id的Target")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "删除指定id的Target成功"
    )})
    @Log(value="删除指定id的Target")
    public void deleteTarget(@PathVariable("id") String id, @PathVariable("tid") String tid) {
        // 调用接口删除目标
        kongRouterClient.getTargetService().deleteTarget(id, tid);
    }

    @DeleteMapping("/{id}/targets")
    @ApiOperation("删除指定id的Target")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "删除指定id的Target成功"
    )})
    @Log(value="删除指定id的Target")
    public void deleteTargets(@PathVariable("id") String id, @RequestParam String ids) {
        if(StringUtils.isEmpty(ids)) return;

        String idList[] = ids.split(",");
        // 调用接口删除目标
        for(String tid : idList) {
            kongRouterClient.getTargetService().deleteTarget(id, tid);
        }
    }
}
