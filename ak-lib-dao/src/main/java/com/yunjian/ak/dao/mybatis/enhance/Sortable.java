package com.yunjian.ak.dao.mybatis.enhance;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 10:12
 * @Version 1.0
 */
public class Sortable implements Serializable {
    private static final long serialVersionUID = -8949730506220263868L;
    private List<Order> orders;

    public Sortable() {
    }

    public Sortable(Order... orders) {
        if (orders != null) {
            this.orders = Lists.newArrayList(orders);
        }

    }

    public List<Order> getOrders() {
        return this.orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
