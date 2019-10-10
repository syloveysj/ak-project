package com.yunjian.ak.gateway.config;

import com.yunjian.ak.config.ConfigManager;
import com.yunjian.ak.kong.client.impl.KongClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/10 22:29
 * @Version 1.0
 */
@Configuration
public class GatewayConfig {

    @Bean
    public KongClient kongClient() {
        KongClient kongClient = new KongClient(ConfigManager.getInstance().getConfig("kong_admin_url"));
        return kongClient;
    }
}
