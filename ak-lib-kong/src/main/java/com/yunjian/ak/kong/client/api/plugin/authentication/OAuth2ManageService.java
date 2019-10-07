package com.yunjian.ak.kong.client.api.plugin.authentication;


import com.yunjian.ak.kong.client.model.plugin.authentication.oauth2.Application;
import com.yunjian.ak.kong.client.model.plugin.authentication.oauth2.ApplicationList;
import com.yunjian.ak.kong.client.model.plugin.authentication.oauth2.Token;
import com.yunjian.ak.kong.client.model.plugin.authentication.oauth2.TokenList;

public interface OAuth2ManageService {

    // App Management ---------------------------------------------------------------------------------------------------

    Application createConsumerApplication(String consumerId, Application request);

    Application getConsumerApplication(String consumerId, String applicationId);

    Application updateConsumerApplication(String consumerId, String applicationId, Application request);

    void deleteConsumerApplication(String consumerId, String applicationId);

    ApplicationList listConsumerApplications(String consumerId);

    ApplicationList listClientApplications(String applicationId, String applicatonName, String clientId, String clientSecret, String consumerId);

    // Token Management ---------------------------------------------------------------------------------------------------

    Token createToken(Token request);

    Token getToken(String tokenId);

    Token updateToken(String tokenId, Token request);

    TokenList listTokens();

}
