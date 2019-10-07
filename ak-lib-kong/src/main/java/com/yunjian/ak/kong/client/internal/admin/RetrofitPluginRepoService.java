package com.yunjian.ak.kong.client.internal.admin;

import com.yunjian.ak.kong.client.model.admin.plugin.EnabledPlugins;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by fanhua on 2017-08-05.
 */
public interface RetrofitPluginRepoService {

    @GET("/plugins/enabled")
    Call<EnabledPlugins> retrieveEnabledPlugins();


    @GET("/plugins/schema/{plugin}")
    Call<Object> retrievePluginSchema(@Path("plugin") String pluginName);
}
