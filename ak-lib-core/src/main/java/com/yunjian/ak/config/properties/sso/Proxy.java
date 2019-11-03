package com.yunjian.ak.config.properties.sso;

import lombok.Data;

/**
 * @Description: Api代理配置
 * @Author: yong.sun
 * @Date: 2019/10/5 19:52
 * @Version 1.0
 */
@Data
public class Proxy {
    /**
     * 代理网关配置
     */
    private Apigateway apigateway;
}
