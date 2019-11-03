package com.yunjian.ak.config.properties.sso;

import lombok.Data;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/5 19:52
 * @Version 1.0
 */
@Data
public class Apigateway {
    /**
     * 访问模式(http, https)
     */
    private String schema = "http";

    /**
     * 主机地址(如：127.0.0.1)
     */
    private String host;

    /**
     * 端口(如：8000)
     */
    private int port = 8000;

    /**
     * 网关访问的apikey
     */
    private String apikey;

    /**
     * 截取uri的正则表达式
     */
    private String pattern = "^(.*/proxy)(.*)$";
}
