package com.yunjian.ak.config.properties;

import com.yunjian.ak.config.properties.sso.Proxy;
import lombok.Data;

/**
 * @Description: SSO 配置
 * @Author: yong.sun
 * @Date: 2019/10/5 19:46
 * @Version 1.0
 */
@Data
public class SSO {
    /**
     * Api代理配置
     */
    private Proxy proxy;
}