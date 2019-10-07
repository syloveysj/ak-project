package com.yunjian.ak.kong.client.api.plugin.authentication;

/**
 * Created by vaibhav on 15/06/17.
 */
public interface BasicAuthService {
    void addCredentials(String consumerIdOrUsername, String username, String password);
}
