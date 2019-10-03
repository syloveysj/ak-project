package com.yunjian.ak.context;

import org.apache.commons.collections4.MapUtils;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/6/25 15:53
 * @Version 1.0
 */
public class DefaultAppOperationContext extends DefaultOperationContext implements AppOperationContext {
    @Override
    public void setTenantScheme(String tenantScheme) {
        this.put("__tenantScheme", tenantScheme);
    }

    @Override
    public String getTenantScheme() {
        return MapUtils.getString(this, "__tenantScheme");
    }

    @Override
    public void setUserId(String userId) {
        this.put("__userId", userId);
    }

    @Override
    public String getUserId() {
        return MapUtils.getString(this, "__userId");
    }

    @Override
    public void setLoginName(String loginName) {
        this.put("__loginName", loginName);
    }

    @Override
    public String getLoginName() {
        return MapUtils.getString(this, "__loginName");
    }

    @Override
    public void setSessionId(String sessionId) {
        this.put("__sessionId", sessionId);
    }

    @Override
    public String getSessionId() {
        return MapUtils.getString(this, "__sessionId");
    }

}
