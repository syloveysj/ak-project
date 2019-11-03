package com.yunjian.ak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Description: AK 云平台服务
 * @Author: yong.sun
 * @Date: 2019/11/3 23:01
 * @Version 1.0
 */
@SpringBootApplication
@ServletComponentScan
@EnableSwagger2
public class PlatformApp {
    /**
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(PlatformApp.class, args);
    }
}
