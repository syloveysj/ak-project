package com.yunjian.ak.web.init;

import javax.servlet.ServletContext;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 17:47
 * @Version 1.0
 */
public interface ServerStartup {
    void startup(ServletContext context);

    void close();
}
