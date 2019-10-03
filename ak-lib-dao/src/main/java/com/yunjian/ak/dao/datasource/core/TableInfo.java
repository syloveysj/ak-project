package com.yunjian.ak.dao.datasource.core;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/22 14:59
 * @Version 1.0
 */
public interface TableInfo {
    String getDs();

    void setDs(String ds);

    String getTable();

    void setTable(String table);
}
