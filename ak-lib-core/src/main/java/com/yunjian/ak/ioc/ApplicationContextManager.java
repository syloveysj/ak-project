package com.yunjian.ak.ioc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 12:19
 * @Version 1.0
 */
public class ApplicationContextManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationContextManager.class);
    private static ApplicationContext context;

    public ApplicationContextManager() {
    }

    public static void init(ApplicationContext webContext) {
        context = webContext;
        LOGGER.info("ApplicationContext初始化完成");
    }

    public static ApplicationContext getContext() {
        if (context == null) {
            LOGGER.error("ApplicationContext还未完初始化!");
        }

        return context;
    }
}
