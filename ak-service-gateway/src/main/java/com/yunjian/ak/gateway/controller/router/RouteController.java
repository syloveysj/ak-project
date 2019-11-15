package com.yunjian.ak.gateway.controller.router;

import com.alibaba.fastjson.JSON;
import com.yunjian.ak.dao.mybatis.enhance.Page;
import com.yunjian.ak.gateway.entity.router.RouteEntity;
import com.yunjian.ak.gateway.service.router.RouteService;
import com.yunjian.ak.gateway.service.router.ServiceService;
import com.yunjian.ak.kong.client.impl.KongClient;
import com.yunjian.ak.kong.client.model.admin.route.Route;
import com.yunjian.ak.kong.client.model.admin.service.Service;
import com.yunjian.ak.web.aspect.Log;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/3 23:11
 * @Version 1.0
 */
@RestController
@RequestMapping("/v1/mgr/gateway/routes")
public class RouteController {

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private KongClient kongRouterClient;

    @PostMapping
    @ApiOperation("添加Route")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "添加Route成功",
            response = RouteEntity.class
    )})
    @Log(value="添加Route")
    public RouteEntity insert(@Valid @RequestBody RouteEntity entity) {
        // 调用接口添加服务
        Service service = new Service();
        BeanUtils.copyProperties(entity.getService(), service);
        service = kongRouterClient.getServiceService().createService(service);

        // 调用接口添加路由
        Route route = new Route();
        BeanUtils.copyProperties(entity, route);
        route.setService(service);
        route = kongRouterClient.getRouteService().createRoute(route);

        // 通过数据库更新别名
        entity.getService().setId(service.getId());
        this.serviceService.update(entity.getService());
        entity.setId(route.getId());
        this.routeService.update(entity);

        return entity;
    }

    @PutMapping("/{id}")
    @ApiOperation("更新Route")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "更新Route成功",
            response = RouteEntity.class
    )})
    @Log(value="更新Route")
    public RouteEntity update(@Valid @RequestBody RouteEntity entity) {
        // 调用接口更新服务
        Service service = new Service();
        BeanUtils.copyProperties(entity.getService(), service);
        service = kongRouterClient.getServiceService().updateService(entity.getService().getId(), service);

        // 调用接口更新路由
        Route route = new Route();
        BeanUtils.copyProperties(entity, route);
        route.setService(service);
        route = kongRouterClient.getRouteService().updateRoute(entity.getId(), route);

        // 通过数据库更新别名
        this.serviceService.update(entity.getService());
        this.routeService.update(entity);

        return entity;
    }

    @DeleteMapping("/{id}/services/{serviceId}")
    @ApiOperation("删除指定id的Route")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "删除指定id的Route成功"
    )})
    @Log(value="删除指定id的Route")
    public void delete(@PathVariable("id") String id, @PathVariable("serviceId") String serviceId) {
        // 调用接口删除路由
        kongRouterClient.getRouteService().deleteRoute(id);
        kongRouterClient.getServiceService().deleteService(serviceId);
    }

    @DeleteMapping
    @ApiOperation("删除指定ids的Route")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "删除指定ids的Route成功"
    )})
    @Log(value="删除指定ids的Route")
    public void deleteList(@RequestParam String ids) {
        // 调用接口删除路由
        List<Map> idList = JSON.parseObject(ids, List.class);
        for (Map id : idList) {
            kongRouterClient.getRouteService().deleteRoute(MapUtils.getString(id, "routeId"));
            kongRouterClient.getServiceService().deleteService(MapUtils.getString(id, "serviceId"));
        }
    }

    @GetMapping
    @ApiOperation("获取匹配Route列表")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "获取Route列表成功",
            response = Page.class
    )})
    @Log(value="获取匹配Route列表")
    public Page<RouteEntity> getListByPage(@RequestParam("page") int page, @RequestParam("pagesize") int pagesize,
                                              @RequestParam(value = "sort", required = false) String sort, @RequestParam(value = "order", required = false) String order,
                                              @RequestParam(value = "cond", required = false) String cond) {
        return this.routeService.getListByPage(page, pagesize, sort, order, cond);
    }
}
