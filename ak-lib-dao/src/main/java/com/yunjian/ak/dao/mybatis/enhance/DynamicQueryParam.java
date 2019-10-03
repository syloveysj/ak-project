package com.yunjian.ak.dao.mybatis.enhance;

import java.io.Serializable;
import java.util.Map;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 10:14
 * @Version 1.0
 */
public class DynamicQueryParam implements Serializable {
    private static final long serialVersionUID = -386481743843772926L;
    private String conditionSql;
    private Map<String, String> queryData;

    public DynamicQueryParam() {
    }

    public DynamicQueryParam(String conditionSql, Map<String, String> queryData) {
        this.conditionSql = conditionSql;
        this.queryData = queryData;
    }

    public String getConditionSql() {
        return this.conditionSql;
    }

    public void setConditionSql(String conditionSql) {
        this.conditionSql = conditionSql;
    }

    public Map<String, String> getQueryData() {
        return this.queryData;
    }

    public void setQueryData(Map<String, String> queryData) {
        this.queryData = queryData;
    }

    public String toString() {
        return "DynamicQueryParam{conditionSql='" + this.conditionSql + "', queryData=" + this.queryData + "}";
    }
}