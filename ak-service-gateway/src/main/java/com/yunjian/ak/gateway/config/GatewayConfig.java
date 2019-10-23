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
    public KongClient kongRouterClient() {
        KongClient kongRouterClient = new KongClient(ConfigManager.getInstance().getConfig("kong_router_admin_url"));
        return kongRouterClient;
    }

    @Bean
    public KongClient kongApisClient() {
        KongClient kongApisClient = new KongClient(ConfigManager.getInstance().getConfig("kong_apis_admin_url"));
        return kongApisClient;
    }
}
