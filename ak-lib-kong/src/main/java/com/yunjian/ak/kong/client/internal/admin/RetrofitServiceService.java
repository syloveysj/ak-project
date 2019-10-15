package com.yunjian.ak.kong.client.internal.admin;

import com.yunjian.ak.kong.client.model.admin.service.Service;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/9/26 15:49
 * @Version 1.0
 */
public interface RetrofitServiceService {

    @POST("services/")
    Call<Service> createService(@Body Service request);

    @PUT("services/{nameOrId}")
    Call<Service> updateService(@Path("nameOrId") String nameOrId, @Body Service service);

    @DELETE("services/{nameOrId}")
    Call<Void> deleteService(@Path("nameOrId") String nameOrId);
}
