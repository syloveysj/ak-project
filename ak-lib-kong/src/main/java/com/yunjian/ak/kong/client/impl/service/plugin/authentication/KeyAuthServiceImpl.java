package com.yunjian.ak.kong.client.impl.service.plugin.authentication;

import com.yunjian.ak.kong.client.api.plugin.authentication.KeyAuthService;
import com.yunjian.ak.kong.client.exception.KongClientException;
import com.yunjian.ak.kong.client.internal.plugin.authentication.RetrofitKeyAuthService;
import com.yunjian.ak.kong.client.model.plugin.authentication.key.KeyAuthCredential;
import com.yunjian.ak.kong.client.model.plugin.authentication.key.KeyAuthCredentialList;
import retrofit2.Response;

import java.io.IOException;

/**
 * Created by vaibhav on 15/06/17.
 * <p>
 * Updated by fanhua on 2017-08-07.
 * <p>
 * Updated by dvilela on 17/10/17.
 */
public class KeyAuthServiceImpl implements KeyAuthService {

    private RetrofitKeyAuthService retrofitKeyAuthService;

    public KeyAuthServiceImpl(RetrofitKeyAuthService retrofitKeyAuthService) {
        this.retrofitKeyAuthService = retrofitKeyAuthService;
    }

    @Override
    public KeyAuthCredential addCredentials(String consumerIdOrUsername, String key) {
        try {
            Response<KeyAuthCredential> res = retrofitKeyAuthService.addCredentials(consumerIdOrUsername,
                    new KeyAuthCredential(key)).execute();
            if (res.code() == 201) {
                return res.body();
            }
            throw new KongClientException("Could not create credentials", res.code(), res.message());
        } catch (IOException e) {
            throw new KongClientException(e.getMessage());
        }
    }

    @Override
    public KeyAuthCredentialList listCredentials(String consumerIdOrUsername, Long size, String offset) {
        try {
            return retrofitKeyAuthService.listCredentials(consumerIdOrUsername, size, offset).execute().body();
        } catch (IOException e) {
            throw new KongClientException(e.getMessage());
        }
    }

    @Override
    public void deleteCredential(String consumerIdOrUsername, String id) {
        try {
            retrofitKeyAuthService.deleteCredential(consumerIdOrUsername, id).execute();
        } catch (IOException e) {
            throw new KongClientException(e.getMessage());
        }
    }
}
