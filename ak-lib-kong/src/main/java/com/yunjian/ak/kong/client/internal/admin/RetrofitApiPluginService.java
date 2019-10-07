package com.yunjian.ak.kong.client.internal.admin;

import com.yunjian.ak.kong.client.model.admin.plugin.Plugin;
import com.yunjian.ak.kong.client.model.admin.plugin.PluginList;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by fanhua on 2017-08-05.
 */
public interface RetrofitApiPluginService {


    @POST("/apis/{api}/plugins")
    Call<Plugin> addPluginForApi(@Path("api") String apiNameOrId, @Body Plugin plugin);

    @GET("/apis/{api}/plugins/{id}")
    Call<Plugin> getPluginForApi(@Path("api") String apiNameOrId, @Path("id") String pluginId);

    @PATCH("/apis/{api}/plugins/{id}")
    Call<Plugin> updatePluginForApi(@Path("api") String apiNameOrId, @Path("id") String pluginNameOrId, @Body Plugin request);

    @PUT("/apis/{api}/plugins")
    Call<Plugin> createOrUpdatePluginForApi(@Path("api") String apiNameOrId, @Body Plugin plugin);

    @DELETE("/apis/{api}/plugins/{id}")
    Call<Void> deletePluginForApi(@Path("api") String apiNameOrId, @Path("id") String pluginNameOrId);

    @GET("/apis/{api}/plugins/")
    Call<PluginList> listPluginsForApi(@Path("api") String apiNameOrId, @Query("id") String pluginNameOrId, @Query("api_id") String apiId,
                                       @Query("consumer_id") String consumerId, @Query("name") String name, @Query("size") Long size, @Query("offset") String offset);


}
