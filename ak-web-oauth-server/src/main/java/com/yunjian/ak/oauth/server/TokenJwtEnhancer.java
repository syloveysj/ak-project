package com.yunjian.ak.oauth.server;

import com.yunjian.ak.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/28 21:50
 * @Version 1.0
 */
public class TokenJwtEnhancer implements TokenEnhancer {

    @Autowired
    private HttpServletRequest request;

    @Override
    public OAuth2AccessToken enhance(
            OAuth2AccessToken accessToken,
            OAuth2Authentication authentication) {

        System.out.println(CommonUtil.urlToDomain(request.getHeader("Origin")));
        User user = (User) authentication.getPrincipal();
        System.out.println("TokenJwtEnhancer ===> " + user.getUsername());

        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("organization", authentication.getName() + randomAlphabetic(4));
        additionalInfo.put("test", "xyz");
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
