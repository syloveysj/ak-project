package com.yunjian.ak.config;

import com.yunjian.ak.config.properties.SSO;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/5 18:30
 * @Version 1.0
 */
@Component
@PropertySource(value = {"classpath:conf/ak-config.properties"}, encoding = "utf-8")
@ConfigurationProperties(prefix = "ak")
@Data
public class AkConfigProperties {
    /**
     * SSO 配置
     */
    private SSO sso;
}
