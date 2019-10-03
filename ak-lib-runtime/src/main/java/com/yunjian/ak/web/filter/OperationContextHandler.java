package com.yunjian.ak.web.filter;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/6/14 10:11
 * @Version 1.0
 */
public interface OperationContextHandler {
    void initContext(HttpServletRequest request);
}
