package com.yunjian.ak.kong.client.impl.service.plugin.authentication;

import com.yunjian.ak.kong.client.api.plugin.authentication.JwtService;
import com.yunjian.ak.kong.client.exception.KongClientException;
import com.yunjian.ak.kong.client.internal.plugin.authentication.RetrofitJwtService;
import com.yunjian.ak.kong.client.model.plugin.authentication.jwt.JwtCredential;
import com.yunjian.ak.kong.client.model.plugin.authentication.jwt.JwtCredentialList;

import java.io.IOException;

/**
 * Created by dvilela on 10/16/17.
 */
public class JwtAuthServiceImpl implements JwtService {

    private RetrofitJwtService retrofitJwtService;

    public JwtAuthServiceImpl(RetrofitJwtService retrofitJwtService) {
        this.retrofitJwtService = retrofitJwtService;
    }

    @Override
    public JwtCredential addCredentials(String consumerIdOrUsername, JwtCredential request) {
        try {
            return retrofitJwtService.addCredentials(consumerIdOrUsername, request).execute().body();
        } catch (IOException e) {
            throw new KongClientException(e.getMessage());
        }
    }

    @Override
    public void deleteCredentials(String consumerIdOrUsername, String id) {
        try {
            retrofitJwtService.deleteCredentials(consumerIdOrUsername, id).execute();
        } catch (IOException e) {
            throw new KongClientException(e.getMessage());
        }
    }

    @Override
    public JwtCredentialList listCredentials(String consumerIdOrUsername, Long size, String offset) {
        try {
            return retrofitJwtService.listCredentials(consumerIdOrUsername, size, offset).execute().body();
        } catch (IOException e) {
            throw new KongClientException(e.getMessage());
        }
    }
}
