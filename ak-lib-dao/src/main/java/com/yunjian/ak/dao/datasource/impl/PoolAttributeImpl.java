package com.yunjian.ak.dao.datasource.impl;

import com.yunjian.ak.dao.datasource.core.PoolAttribute;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/22 14:57
 * @Version 1.0
 */
public class PoolAttributeImpl implements PoolAttribute {
    protected static final String KEY_EDEFAULT = "";
    protected String key = "";
    protected static final String VALUE_EDEFAULT = null;
    protected String value;
    protected static final String DESC_EDEFAULT = null;
    protected String desc;

    protected PoolAttributeImpl() {
        this.value = VALUE_EDEFAULT;
        this.desc = DESC_EDEFAULT;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String newKey) {
        this.key = newKey;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String newValue) {
        this.value = newValue;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String newDesc) {
        this.desc = newDesc;
    }

    public String toString() {
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (key: ");
        result.append(this.key);
        result.append(", value: ");
        result.append(this.value);
        result.append(", desc: ");
        result.append(this.desc);
        result.append(')');
        return result.toString();
    }
}
