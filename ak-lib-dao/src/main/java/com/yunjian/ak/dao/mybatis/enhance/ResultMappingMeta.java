package com.yunjian.ak.dao.mybatis.enhance;

import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.type.JdbcType;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 9:48
 * @Version 1.0
 */
public class ResultMappingMeta {
    private String columnName;
    private Class<?> javaType;
    private JdbcType jdbcType;
    private Object data;
    private String desc = "";

    public ResultMappingMeta(String columnName, Class<?> javaType, JdbcType jdbcType, Object data, String desc) {
        this.columnName = columnName;
        this.javaType = javaType;
        this.jdbcType = jdbcType;
        this.data = data;
        this.desc = desc;
    }

    public ResultMappingMeta(String columnName, ResultMapping rm) {
        this.columnName = columnName;
        if (rm.getJavaType() == null) {
            this.desc = "(默认)";
        }

        this.javaType = rm.getJavaType() == null ? String.class : rm.getJavaType();
        this.jdbcType = rm.getJdbcType() == null ? JdbcType.VARCHAR : rm.getJdbcType();
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public Class<?> getJavaType() {
        return this.javaType;
    }

    public JdbcType getJdbcType() {
        return this.jdbcType;
    }

    public String getJavaTypeStr() {
        return this.javaType.toString() + this.desc;
    }

    public ResultMappingMeta clone() {
        return new ResultMappingMeta(this.columnName, this.javaType, this.jdbcType, this.data, this.desc);
    }
}

