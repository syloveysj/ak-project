package com.yunjian.ak.kong.client.api.plugin.authentication;

import com.yunjian.ak.kong.client.model.plugin.authentication.jwt.JwtCredential;
import com.yunjian.ak.kong.client.model.plugin.authentication.jwt.JwtCredentialList;

/**
 * Created by vaibhav on 16/06/17.
 * <p>
 * Updated by dvilela on 17/10/17.
 */
public interface JwtService {
    JwtCredential addCredentials(String consumerIdOrUsername, JwtCredential request);

    void deleteCredentials(String consumerIdOrUsername, String id);

    JwtCredentialList listCredentials(String consumerIdOrUsername, Long size, String offset);
}
