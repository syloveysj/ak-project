package com.yunjian.ak.context;

import sun.rmi.runtime.Log;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 13:35
 * @Version 1.0
 */
public interface WebOperationContext extends OperationContext {
    void setRequest(HttpServletRequest request);

    HttpServletRequest getRequest();

    HttpServletResponse getResponse();

    HttpSession getSession();

    Subject getUser();

//    SystemVariable getSystemVariable();

    /** @deprecated */
    @Deprecated
    Map<String, Object> getInput();

    Object getBody();

    Map<String, Object> getPathParams();

    Map<String, Object> getQueryParams();

    String getPermissionID();

    Log getLog();
}
