package com.yunjian.ak.web.filter;

import com.yunjian.ak.context.AppOperationContext;
import com.yunjian.ak.context.OperationContextHolder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/6/14 10:15
 * @Version 1.0
 */
public class DefaultOperationContextHandler implements OperationContextHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultOperationContextHandler.class);

    @Override
    public void initContext(HttpServletRequest request) {
        AppOperationContext context = (AppOperationContext) OperationContextHolder.getContext();
        if(StringUtils.isNotEmpty(request.getHeader(AppOperationContext.AK_TENANT_SCHEME))) context.setTenantScheme(request.getHeader(AppOperationContext.AK_TENANT_SCHEME));
        if(StringUtils.isNotEmpty(request.getHeader(AppOperationContext.AK_USER_ID))) context.setUserId(request.getHeader(AppOperationContext.AK_USER_ID));
        if(StringUtils.isNotEmpty(request.getHeader(AppOperationContext.AK_LOGIN_NAME))) context.setLoginName(request.getHeader(AppOperationContext.AK_LOGIN_NAME));
        if(StringUtils.isNotEmpty(request.getHeader(AppOperationContext.AK_SESSION_ID))) context.setSessionId(request.getHeader(AppOperationContext.AK_SESSION_ID));

        LOGGER.debug("当前Scheme : {}", context.getTenantScheme());
    }

}
