package com.yunjian.ak.test.spring.service;

import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/11/8 22:47
 * @Version 1.0
 */
@Service
public class MyService {
    public String sayHello(String name) {
        return "hello " + name;
    }
}
