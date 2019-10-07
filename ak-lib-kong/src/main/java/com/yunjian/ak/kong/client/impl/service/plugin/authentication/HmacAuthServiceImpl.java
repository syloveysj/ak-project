package com.yunjian.ak.kong.client.impl.service.plugin.authentication;

import com.yunjian.ak.kong.client.api.plugin.authentication.HmacAuthService;
import com.yunjian.ak.kong.client.exception.KongClientException;
import com.yunjian.ak.kong.client.internal.plugin.authentication.RetrofitHmacAuthService;
import com.yunjian.ak.kong.client.model.plugin.authentication.hmac.HmacAuthCredential;

import java.io.IOException;

/**
 * Created by vaibhav on 15/06/17.
 * <p>
 * Updated by fanhua on 2017-08-07.
 */
public class HmacAuthServiceImpl implements HmacAuthService {

    private RetrofitHmacAuthService retrofitHmacAuthService;

    public HmacAuthServiceImpl(RetrofitHmacAuthService retrofitHmacAuthService) {
        this.retrofitHmacAuthService = retrofitHmacAuthService;
    }

    @Override
    public void addCredentials(String consumerIdOrUsername, String username, String secret) {
        try {
            retrofitHmacAuthService.addCredentials(consumerIdOrUsername, new HmacAuthCredential(username, secret)).execute();
        } catch (IOException e) {
            throw new KongClientException(e.getMessage());
        }
    }
}
