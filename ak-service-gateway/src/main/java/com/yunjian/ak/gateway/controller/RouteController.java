package com.yunjian.ak.gateway.controller;

import com.yunjian.ak.dao.mybatis.enhance.Page;
import com.yunjian.ak.gateway.entity.RouteEntity;
import com.yunjian.ak.gateway.entity.ServiceEntity;
import com.yunjian.ak.gateway.service.RouteService;
import com.yunjian.ak.gateway.service.ServiceService;
import com.yunjian.ak.kong.client.impl.KongClient;
import com.yunjian.ak.kong.client.model.admin.route.Route;
import com.yunjian.ak.kong.client.model.admin.service.Service;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/3 23:11
 * @Version 1.0
 */
@RestController
@RequestMapping("/v1/mgr/gateway/routes")
public class RouteController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpstreamController.class);

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private KongClient kongClient;

    @PostMapping
    @ApiOperation("添加Route")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "添加Route成功",
            response = ServiceEntity.class
    )})
    public ServiceEntity insert(@Valid @RequestBody ServiceEntity entity) {
        LOGGER.debug("请求 RouteController 的 insert!");

        // 调用接口添加服务
        Service request = new Service();
        BeanUtils.copyProperties(entity, request);
        Service result = kongClient.getServiceService().createService(request);

        List<RouteEntity> routeList = new ArrayList<>();
        // 调用接口添加路由
        if(entity.getRoutes() != null && !entity.getRoutes().isEmpty()){
            for (RouteEntity routeEntity : entity.getRoutes()) {
                Route route = new Route();
                BeanUtils.copyProperties(routeEntity, route);
                route.setService(new Service());
                route.getService().setId(result.getId());
                Route ret = kongClient.getRouteService().createRoute(route);

                routeEntity.setId(ret.getId());
                routeList.add(routeEntity);
            }
        }

        // 通过数据库更新别名
        ServiceEntity service = new ServiceEntity();
        service.setId(result.getId());
        service.setAlias(entity.getAlias());
        this.serviceService.update(service);
        this.routeService.updateBatch(routeList);

        return service;
    }

    @PutMapping("/{id}")
    @ApiOperation("更新Route")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "更新Route成功",
            response = ServiceEntity.class
    )})
    public ServiceEntity update(@Valid @RequestBody ServiceEntity entity) {
        LOGGER.debug("请求 RouteController 的 update!");

        // 调用接口添加服务
        Service request = new Service();
        BeanUtils.copyProperties(entity, request);
        Service result = kongClient.getServiceService().updateService(entity.getId(), request);

        // 调用接口添加路由
        if(entity.getRoutes() != null && !entity.getRoutes().isEmpty()){
            for (RouteEntity routeEntity : entity.getRoutes()) {
                Route route = new Route();
                BeanUtils.copyProperties(routeEntity, route);
                route.setService(new Service());
                route.getService().setId(result.getId());
                kongClient.getRouteService().updateRoute(routeEntity.getId(), route);
            }
        }

        // 通过数据库更新别名
        this.serviceService.update(entity);
        this.routeService.updateBatch(entity.getRoutes());

        return entity;
    }

    @DeleteMapping("/{id}/services/{serviceId}")
    @ApiOperation("删除指定id的Route")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "删除指定id的Route成功"
    )})
    public void delete(@PathVariable("id") String id, @PathVariable("serviceId") String serviceId) {
        LOGGER.debug("请求RouteController删除指定id的Route:{}!", id);

        // 调用接口删除路由
        kongClient.getRouteService().deleteRoute(id);
        kongClient.getServiceService().deleteService(serviceId);
    }

    @GetMapping
    @ApiOperation("获取匹配Route列表")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "获取Route列表成功",
            response = Page.class
    )})
    public Page<RouteEntity> getListByPage(@RequestParam("page") int page, @RequestParam("pagesize") int pagesize,
                                              @RequestParam(value = "sort", required = false) String sort, @RequestParam(value = "order", required = false) String order,
                                              @RequestParam(value = "cond", required = false) String cond) {
        LOGGER.debug("请求RouteController获取匹配Route列表!");

        return this.routeService.getListByPage(page, pagesize, sort, order, cond);
    }
}
