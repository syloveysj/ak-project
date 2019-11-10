package com.yunjian.ak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/11/8 22:43
 * @Version 1.0
 */
@SpringBootApplication
@ServletComponentScan
public class TestApp {
    /**
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(TestApp.class, args);
    }
}