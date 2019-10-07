package com.yunjian.ak.kong.client.internal.plugin.authentication;

import com.yunjian.ak.kong.client.model.plugin.authentication.hmac.HmacAuthCredential;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by vaibhav on 15/06/17.
 */
public interface RetrofitHmacAuthService {

    @POST("consumers/{id}/hmac-auth")
    Call<HmacAuthCredential> addCredentials(@Path("id") String consumerIdOrUsername, @Body HmacAuthCredential request);
}
