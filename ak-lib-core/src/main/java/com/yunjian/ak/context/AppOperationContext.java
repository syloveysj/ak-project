package com.yunjian.ak.context;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/6/25 15:50
 * @Version 1.0
 */
public interface AppOperationContext extends OperationContext {
    final public static String AK_TENANT_SCHEME = "AK-TENANT-SCHEME";
    final public static String AK_USER_ID = "AK-USER-ID";
    final public static String AK_LOGIN_NAME = "AK-LOGIN-NAME";
    final public static String AK_SESSION_ID = "AK-SESSION-ID";

    void setTenantScheme(String tenantScheme);
    String getTenantScheme();

    void setUserId(String userId);
    String getUserId();

    void setLoginName(String loginName);
    String getLoginName();

    void setSessionId(String sessionId);
    String getSessionId();

}
