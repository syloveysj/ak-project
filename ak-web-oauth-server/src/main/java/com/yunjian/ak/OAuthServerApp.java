package com.yunjian.ak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @Description: OAuth2 认证服务器应用
 * @Author: yong.sun
 * @Date: 2019/10/28 21:23
 * @Version 1.0
 */
@SpringBootApplication
@ServletComponentScan
public class OAuthServerApp {

    /**
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(OAuthServerApp.class, args);
    }
}
