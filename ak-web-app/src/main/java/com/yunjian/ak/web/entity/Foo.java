package com.yunjian.ak.web.entity;

import lombok.Data;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/11/1 20:13
 * @Version 1.0
 */
@Data
public class Foo {
    private long id;
    private String name;

    public Foo(long parseLong, String randomAlphanumeric) {
        this.id = parseLong;
        this.name = randomAlphanumeric;
    }
}
