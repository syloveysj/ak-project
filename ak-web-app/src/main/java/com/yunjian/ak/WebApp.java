package com.yunjian.ak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/11/1 19:52
 * @Version 1.0
 */
@SpringBootApplication
@ServletComponentScan
public class WebApp {

    /**
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(WebApp.class, args);
    }
}
