package com.yunjian.ak.rs.config;

import com.yunjian.ak.context.AppOperationContext;
import com.yunjian.ak.context.OperationContextHolder;
import org.apache.commons.collections4.MapUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CustomAccessTokenConverter extends DefaultAccessTokenConverter {

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> claims) {
        OAuth2Authentication authentication = super.extractAuthentication(claims);
        authentication.setDetails(claims);

        // 获取为JWT扩展的信息
        AppOperationContext context = (AppOperationContext) OperationContextHolder.getContext();
        context.setTenantScheme(MapUtils.getString(claims, "scheme"));
        context.setUserId(MapUtils.getString(claims, "uid"));
        context.setLoginName(MapUtils.getString(claims, "loginName"));
        context.setSessionId(MapUtils.getString(claims, "jti"));

        return authentication;
    }

}
