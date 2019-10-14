package com.yunjian.ak.kong.client.internal.admin;

import com.yunjian.ak.kong.client.model.admin.route.Route;
import com.yunjian.ak.kong.client.model.admin.route.RouteList;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/9/26 15:52
 * @Version 1.0
 */
public interface RetrofitRouteService {
    @POST("routes/")
    Call<Route> createRoute(@Body Route route);

    @POST("/services/{serviceNameOrId}/routes")
    Call<Route> createRoute(@Path("serviceNameOrId") String serviceNameOrId, @Body Route route);

    @PUT("routes/{nameOrId}")
    Call<Route> updateRoute(@Path("nameOrId") String nameOrId, @Body Route route);

    @DELETE("routes/{nameOrId}")
    Call<Void> deleteRoute(@Path("nameOrId") String nameOrId);

    @GET("routes/")
    Call<RouteList> listRoutes(@Query("id") String id, @Query("slots") Integer slots, @Query("name") String name, @Query("size") Long size, @Query("offset") String offset);

    @GET("services/{id}/routes")
    Call<RouteList> listRoutesByService(@Path("id") String serviceNameOrId, @Query("id") String id, @Query("slots") Integer slots, @Query("name") String name, @Query("size") Long size, @Query("offset") String offset);
}
