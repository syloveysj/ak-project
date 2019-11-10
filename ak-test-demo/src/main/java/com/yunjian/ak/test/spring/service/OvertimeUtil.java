package com.yunjian.ak.test.spring.service;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/11/8 22:51
 * @Version 1.0
 */
public class OvertimeUtil {
    @Autowired
    MyService myService;

    public void opt(String name) {
        myService.sayHello(name);
    }

}
