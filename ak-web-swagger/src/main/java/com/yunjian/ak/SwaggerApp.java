package com.yunjian.ak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/28 14:02
 * @Version 1.0
 */
@SpringBootApplication
@ServletComponentScan
public class SwaggerApp {
    /**
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(SwaggerApp.class, args);
    }
}
