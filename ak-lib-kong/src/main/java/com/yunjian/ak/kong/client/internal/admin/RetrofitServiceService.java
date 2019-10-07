package com.yunjian.ak.kong.client.internal.admin;

import com.yunjian.ak.kong.client.model.admin.service.Service;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/9/26 15:49
 * @Version 1.0
 */
public interface RetrofitServiceService {

    @POST("services/")
    Call<Service> addService(@Body Service request);

    @DELETE("services/{nameOrId}")
    Call<Void> deleteService(@Path("nameOrId") String nameOrId);
}
