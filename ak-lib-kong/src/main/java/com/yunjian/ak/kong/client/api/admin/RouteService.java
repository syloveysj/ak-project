package com.yunjian.ak.kong.client.api.admin;

import com.yunjian.ak.kong.client.model.admin.route.Route;
import com.yunjian.ak.kong.client.model.admin.route.RouteList;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/9/26 15:52
 * @Version 1.0
 */
public interface RouteService {
    Route createRoute(Route route);

    Route createRoute(String serviceNameOrId, Route route);

    Route updateRoute(String nameOrId, Route route);

    void deleteRoute(String nameOrId);

    RouteList listRoutes(String id, Integer slots, String name, Long size, String offset);

    RouteList listRoutesByService(String serviceNameOrId, String id, Integer slots, String name, Long size, String offset);
}
