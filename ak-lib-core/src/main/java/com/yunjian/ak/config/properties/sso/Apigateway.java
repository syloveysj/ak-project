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
    private String schema;
    private String host;
    private int port;
    private String apikey;
}
