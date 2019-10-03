package com.yunjian.ak.context;

import org.springframework.context.ApplicationContext;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 13:36
 * @Version 1.0
 */
public class ApplicationContextWrapper {
    private ApplicationContext ctx;

    public ApplicationContextWrapper(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    public Object getBean(String beanName) {
        return this.ctx.getBean(beanName);
    }
}
