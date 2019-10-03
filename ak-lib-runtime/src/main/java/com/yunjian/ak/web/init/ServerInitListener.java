package com.yunjian.ak.web.init;

import com.yunjian.ak.res.ResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @Description: 服务器初始化监听
 * @Author: yong.sun
 * @Date: 2019/5/22 9:34
 * @Version 1.0
 */
@WebListener
public class ServerInitListener implements ServletContextListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerInitListener.class);

    public ServerInitListener() {
    }

    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("平台开始启动...");
        String appRoot = this.getClass().getResource("/").getPath();
        LOGGER.debug("应用根目录：" + appRoot);

        ResourceManager.init(appRoot);
    }

    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("服务器关闭中...");
    }
}
