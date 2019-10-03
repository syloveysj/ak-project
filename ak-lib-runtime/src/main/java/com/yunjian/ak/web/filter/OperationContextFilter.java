package com.yunjian.ak.web.filter;

import com.yunjian.ak.context.OperationContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/29 17:36
 * @Version 1.0
 */
@WebFilter(urlPatterns = "/*", filterName = "operationContextFilter")
public class OperationContextFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperationContextFilter.class);

    @Autowired
    OperationContextHandler operationContextHandler;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");
        LOGGER.debug("path = {}", request.getRequestURI());

//        long start = System.currentTimeMillis();
        try {
            operationContextHandler.initContext(request);
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
//            e.printStackTrace();
        } finally {
            OperationContextHolder.clear();
        }
//        System.out.println("TenantFilter Execute cost=" + (System.currentTimeMillis() - start));
    }

    @Override
    public void destroy() {
//        System.out.println("Filter destroy");
    }
}
