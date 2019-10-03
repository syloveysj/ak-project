package com.yunjian.ak.dao.mybatis;

import java.util.Map;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 11:39
 * @Version 1.0
 */
public class PageEntity {
    private Integer page;
    private Integer size;
    private Map params;
    private String orderColumn;
    private String orderTurn = "ASC";

    public PageEntity() {
    }

    public String getOrderColumn() {
        return this.orderColumn;
    }

    public void setOrderColumn(String orderColumn) {
        this.orderColumn = orderColumn;
    }

    public String getOrderTurn() {
        return this.orderTurn;
    }

    public void setOrderTurn(String orderTurn) {
        this.orderTurn = orderTurn;
    }

    public Integer getPage() {
        return this.page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return this.size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Map getParams() {
        return this.params;
    }

    public void setParams(Map params) {
        this.params = params;
    }
}
