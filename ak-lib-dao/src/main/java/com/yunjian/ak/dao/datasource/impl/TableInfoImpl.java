package com.yunjian.ak.dao.datasource.impl;

import com.yunjian.ak.dao.datasource.core.TableInfo;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/22 14:59
 * @Version 1.0
 */
public class TableInfoImpl implements TableInfo {
    protected static final String DS_EDEFAULT = null;
    protected String ds;
    protected static final String TABLE_EDEFAULT = null;
    protected String table;

    protected TableInfoImpl() {
        this.ds = DS_EDEFAULT;
        this.table = TABLE_EDEFAULT;
    }

    public String getDs() {
        return this.ds;
    }

    public void setDs(String newDs) {
        this.ds = newDs;
    }

    public String getTable() {
        return this.table;
    }

    public void setTable(String newTable) {
        this.table = newTable;
    }

    public String toString() {
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (ds: ");
        result.append(this.ds);
        result.append(", table: ");
        result.append(this.table);
        result.append(')');
        return result.toString();
    }
}
