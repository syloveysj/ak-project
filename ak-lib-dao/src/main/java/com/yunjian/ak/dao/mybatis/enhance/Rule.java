package com.yunjian.ak.dao.mybatis.enhance;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 10:11
 * @Version 1.0
 */
public class Rule {
    private String field;
    private String op;
    private Object data;

    public Rule() {
    }

    public Rule(String field, String op, Object data) {
        this.field = field;
        this.op = op;
        this.data = data;
    }

    public Rule field(String field) {
        this.field = field;
        return this;
    }

    public Rule op(String op) {
        this.op = op;
        return this;
    }

    public Rule data(Object data) {
        this.data = data;
        return this;
    }

    public String getField() {
        return this.field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOp() {
        return this.op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
