package com.yunjian.ak.gateway.entity;

import lombok.Data;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/5 20:56
 * @Version 1.0
 */
@Data
public class GatewayHost {
    // http://192.168.0.175:8001/
    private String url;
    private String username;
    private String password;
}
