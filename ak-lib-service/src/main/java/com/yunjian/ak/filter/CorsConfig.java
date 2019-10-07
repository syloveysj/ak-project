package com.yunjian.ak.filter;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: cors跨域复杂请求有一个预请求，第二个过滤器判断token时会把预请求当做真正的请求去判断，所以在第二个过滤器判断token之前先判断是不是预请求OPTIONS，不是则验证token，是则放行
 * @Author: yong.sun
 * @Date: 2019/6/20 17:13
 * @Version 1.0
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsConfig implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        //允许所有的域访问
        response.setHeader("Access-Control-Allow-Origin", "*");
        //允许所有方式的请求
        response.setHeader("Access-Control-Allow-Methods", "*");
        //头信息缓存有效时长（如果不设 Chromium 同时规定了一个默认值 5 秒），没有缓存将已OPTIONS进行预请求
        response.setHeader("Access-Control-Max-Age", "3600");
        //允许的头信息
        response.setHeader("Access-Control-Allow-Headers", "*");

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void destroy() {}

}
