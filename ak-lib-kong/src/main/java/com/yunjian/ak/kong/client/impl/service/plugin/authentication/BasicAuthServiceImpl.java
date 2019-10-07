package com.yunjian.ak.kong.client.impl.service.plugin.authentication;

import com.yunjian.ak.kong.client.api.plugin.authentication.BasicAuthService;
import com.yunjian.ak.kong.client.exception.KongClientException;
import com.yunjian.ak.kong.client.internal.plugin.authentication.RetrofitBasicAuthService;
import com.yunjian.ak.kong.client.model.plugin.authentication.basic.BasicAuthCredential;

import java.io.IOException;

/**
 * Created by vaibhav on 15/06/17.
 * <p>
 * Updated by fanhua on 2017-08-07.
 */
public class BasicAuthServiceImpl implements BasicAuthService {

    private RetrofitBasicAuthService retrofitBasicAuthService;

    public BasicAuthServiceImpl(RetrofitBasicAuthService retrofitBasicAuthService) {
        this.retrofitBasicAuthService = retrofitBasicAuthService;
    }

    @Override
    public void addCredentials(String consumerIdOrUsername, String username, String password) {
        try {
            retrofitBasicAuthService.addCredentials(consumerIdOrUsername, new BasicAuthCredential(username, password)).execute();
        } catch (IOException e) {
            throw new KongClientException(e.getMessage());
        }
    }
}
