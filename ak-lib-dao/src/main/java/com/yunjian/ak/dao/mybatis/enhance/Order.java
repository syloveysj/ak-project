package com.yunjian.ak.dao.mybatis.enhance;

import java.io.Serializable;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 10:21
 * @Version 1.0
 */
public class Order implements Serializable {
    private static final long serialVersionUID = -5250244790822246576L;
    public static final String DIRECTION_ASC = "ASC";
    public static final String DIRECTION_DESC = "DESC";
    private String orderDirection;
    private String orderProperty;

    public Order() {
    }

    public Order(String orderProperty) {
        this(orderProperty, "ASC");
    }

    public Order(String orderProperty, String orderDirection) {
        this.orderProperty = orderProperty;
        this.orderDirection = orderDirection;
    }

    public String getOrderDirection() {
        return this.orderDirection;
    }

    public void setOrderDirection(String orderDirection) {
        this.orderDirection = orderDirection;
    }

    public String getOrderProperty() {
        return this.orderProperty;
    }

    public void setOrderProperty(String orderProperty) {
        this.orderProperty = orderProperty;
    }
}
