package com.yunjian.ak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Description: 网关服务
 * @Author: yong.sun
 * @Date: 2019/10/3 22:25
 * @Version 1.0
 */
@SpringBootApplication
@ServletComponentScan
@EnableSwagger2
public class GatewayApp {
    /**
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(GatewayApp.class, args);
    }
}
