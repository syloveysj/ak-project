package com.yunjian.ak.oauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/11/2 10:25
 * @Version 1.0
 */
@Controller
public class IssuerController {
    @RequestMapping(method = RequestMethod.GET, value = "/.well-known/openid-configuration")
    @ResponseBody
    public Map issuer(HttpServletRequest request) {
        String path = request.getContextPath();
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int port = request.getServerPort();
        String basePath = scheme + "://" + serverName + ":" + port + path;

        Map content = new HashMap();
        content.put("issuer", basePath);
        content.put("authorization_endpoint", basePath + "/oauth/authorize");
        content.put("token_endpoint", basePath + "/oauth/token");
        content.put("token_endpoint_auth_methods_supported", Arrays.asList("client_secret_basic", "private_key_jwt"));
        content.put("token_endpoint_auth_signing_alg_values_supported", Arrays.asList("RS256", "ES256"));
        content.put("userinfo_endpoint", basePath + "/oauth/userinfo");
        content.put("check_session_iframe", basePath + "/oauth/check_session");
        content.put("end_session_endpoint", basePath + "/oauth/end_session");
        content.put("jwks_uri", basePath + "/.well-known/jwks.json");
        content.put("registration_endpoint", basePath + "/register");
        content.put("scopes_supported", Arrays.asList("openid", "profile", "email", "address", "phone", "offline_access"));
        content.put("response_types_supported", Arrays.asList("code", "code id_token", "id_token", "token id_token"));
        content.put("acr_values_supported", Arrays.asList("urn:mace:incommon:iap:silver", "urn:mace:incommon:iap:bronze"));
        content.put("subject_types_supported", Arrays.asList("public", "pairwise"));
        content.put("userinfo_signing_alg_values_supported", Arrays.asList("RS256", "ES256", "HS256"));
        content.put("userinfo_encryption_alg_values_supported", Arrays.asList("RSA1_5", "A128KW"));
        content.put("userinfo_encryption_enc_values_supported", Arrays.asList("A128CBC-HS256", "A128GCM"));
        content.put("id_token_signing_alg_values_supported", Arrays.asList("RS256", "ES256", "HS256"));
        content.put("id_token_encryption_alg_values_supported", Arrays.asList("RSA1_5", "A128KW"));
        content.put("id_token_encryption_enc_values_supported", Arrays.asList("A128CBC-HS256", "A128GCM"));
        content.put("request_object_signing_alg_values_supported", Arrays.asList("none", "RS256", "ES256"));
        content.put("display_values_supported", Arrays.asList("page", "popup"));
        content.put("claim_types_supported", Arrays.asList("normal", "distributed"));
        content.put("claims_supported", Arrays.asList("sub", "iss", "auth_time", "acr",
                "name", "given_name", "family_name", "nickname",
                "profile", "picture", "website",
                "email", "email_verified", "locale", "zoneinfo"));
        content.put("claims_parameter_supported", true);
        content.put("service_documentation", basePath + "/service_documentation.html");
        content.put("ui_locales_supported", Arrays.asList("en-US", "en-GB", "en-CA", "fr-FR", "fr-CA"));
        return content;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/.well-known/jwks.json")
    @ResponseBody
    public Map jwks(HttpServletRequest request) {
        Map content = new HashMap();
        Map key = new HashMap();
        key.put("kid", "1234example=");
        key.put("alg", "RS256");
        key.put("kty", "RSA");
        key.put("e", "AQAB");
        key.put("n", "1234567890");
        key.put("use", "use");
        content.put("keys", Arrays.asList(key));
        return content;
    }
}
