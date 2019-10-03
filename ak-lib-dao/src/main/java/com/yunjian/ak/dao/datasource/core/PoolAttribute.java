package com.yunjian.ak.dao.datasource.core;

/**
 * @Description: 连接池属性接口
 * @Author: yong.sun
 * @Date: 2019/5/22 9:34
 * @Version 1.0
 */
public interface PoolAttribute {
    String getKey();

    void setKey(String key);

    String getValue();

    void setValue(String value);

    String getDesc();

    void setDesc(String desc);
}
