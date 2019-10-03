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
 * @Date: 2019/5/24 13:34
 * @Version 1.0
 */
public class DefaultWebOperationContext extends DefaultOperationContext implements WebOperationContext {
    private static final long serialVersionUID = 6241393499249369103L;

    public DefaultWebOperationContext() {
    }

    public void setRequest(HttpServletRequest request) {
        this.put("__request", request);
    }

    public HttpServletRequest getRequest() {
        return (HttpServletRequest)this.get("__request");
    }

    public void setResponse(HttpServletResponse response) {
        this.put("__response", response);
    }

    public HttpServletResponse getResponse() {
        return (HttpServletResponse)this.get("__response");
    }

    public HttpSession getSession() {
        HttpServletRequest request = this.getRequest();
        return request != null ? request.getSession() : null;
    }

    public Subject getUser() {
//        AuthenticationProvider provider = (AuthenticationProvider) ApplicationContextManager.getContext().getBean(AuthenticationProvider.class);
//        return provider.getSubject();

        // 获得当前登陆用户对应的对象
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
//                .getAuthentication()
//                .getPrincipal();

        // 当前登陆用户所拥有的所有权限
//        GrantedAuthority[] authorities = userDetails.getAuthorities();

        return null;
    }

//    public SystemVariable getSystemVariable() {
//        return null;
//    }

    public Map<String, Object> getInput() {
        return (Map)this.get("Input");
    }

    public Log getLog() {
        return (Log)this.get("__Log");
    }

    public Object getBody() {
        return (Map)this.get("__Input_Body");
    }

    public Map<String, Object> getPathParams() {
        return (Map)this.get("__Input_PathParams");
    }

    public Map<String, Object> getQueryParams() {
        return (Map)this.get("__Input_QueryParams");
    }

    public String getPermissionID() {
        return (String)this.get("__Resource_PermissionId");
    }
}
