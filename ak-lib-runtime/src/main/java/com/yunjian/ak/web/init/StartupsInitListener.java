package com.yunjian.ak.web.init;

import com.yunjian.ak.ioc.ApplicationContextManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 17:38
 * @Version 1.0
 */
@WebListener
public class StartupsInitListener implements ServletContextListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartupsInitListener.class);

    public StartupsInitListener() {
    }

    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        try {
            ApplicationContextManager.init(WebApplicationContextUtils.getWebApplicationContext(context));
        } catch (Exception ex) {
            LOGGER.error("ApplicationContext初始化失败，平台无法正常启动!");
            return;
        }

        Map<String, ServerStartup> startups = this.getStartups();
        if (!startups.isEmpty()) {
            LOGGER.info("开始初始化扩展模块");
            Iterator iterator = startups.entrySet().iterator();

            while(iterator.hasNext()) {
                Map.Entry startup = (Map.Entry)iterator.next();

                try {
                    ((ServerStartup)startup.getValue()).startup(context);
                } catch (Exception e) {
                    LOGGER.error("扩展模块" + (String)startup.getKey() + "初始化时发生错误：" + e.getMessage());
                }
            }

            LOGGER.info("扩展模块初始化完成");
        }

        LOGGER.info("平台启动完成");
    }

    private Map<String, ServerStartup> getStartups() {
        Map<String, ServerStartup> startups = ApplicationContextManager.getContext().getBeansOfType(ServerStartup.class);
        if (startups == null) {
            startups = Collections.emptyMap();
        }

        return startups;
    }

    public void contextDestroyed(ServletContextEvent sce) {
        Map<String, ServerStartup> startups = this.getStartups();
        if (!startups.isEmpty()) {
            LOGGER.info("扩展模块开始关闭");
            Iterator iterator = startups.entrySet().iterator();

            while(iterator.hasNext()) {
                Map.Entry startup = (Map.Entry)iterator.next();

                try {
                    ((ServerStartup)startup.getValue()).close();
                } catch (Exception e) {
                    LOGGER.error("扩展模块" + (String)startup.getKey() + "关闭时发生错误：" + e.getMessage());
                }
            }

            LOGGER.info("扩展模块关闭完成");
        }

        LOGGER.info("服务器关闭成功");
    }
}
