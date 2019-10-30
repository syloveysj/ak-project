package com.yunjian.ak.swagger.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/28 14:04
 * @Version 1.0
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {

    /**
     *  swagger增加url映射
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/duodian/swagger/**").addResourceLocations("classpath:/swagger/dist/");
    }

}
