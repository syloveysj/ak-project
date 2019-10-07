package com.yunjian.ak.kong.client.internal.plugin.authentication;

import com.yunjian.ak.kong.client.model.plugin.authentication.key.KeyAuthCredential;
import com.yunjian.ak.kong.client.model.plugin.authentication.key.KeyAuthCredentialList;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by vaibhav on 15/06/17.
 * <p>
 * Updated by dvilela on 17/10/17.
 */
public interface RetrofitKeyAuthService {

    @POST("consumers/{id}/key-auth")
    Call<KeyAuthCredential> addCredentials(@Path("id") String consumerIdOrUsername, @Body KeyAuthCredential request);

    @GET("consumers/{id}/key-auth")
    Call<KeyAuthCredentialList> listCredentials(@Path("id") String consumerIdOrUsername, @Query("size") Long size, @Query("offset") String offset);

    @DELETE("consumers/{consumer}/key-auth/{id}")
    Call<Void> deleteCredential(@Path("consumer") String consumer, @Path("id") String id);
}
