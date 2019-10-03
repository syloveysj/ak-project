package com.yunjian.ak.dao.datasource.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Description: 连接池类型枚举
 * @Author: yong.sun
 * @Date: 2019/5/22 9:34
 * @Version 1.0
 */
public enum PoolType {
    DBCP(0, "dbcp", "dbcp"),
    C3P0(1, "c3p0", "c3p0"),
    JNDI(2, "jndi", "jndi"),
    CUSTOM(3, "custom", "custom");

    public static final int DBCP_VALUE = 0;
    public static final int C3P0_VALUE = 1;
    public static final int JNDI_VALUE = 2;
    public static final int CUSTOM_VALUE = 3;
    private static final PoolType[] VALUES_ARRAY = new PoolType[]{DBCP, C3P0, JNDI, CUSTOM};
    public static final List<PoolType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));
    private final int value;
    private final String name;
    private final String literal;

    public static PoolType get(String literal) {
        for(int i = 0; i < VALUES_ARRAY.length; ++i) {
            PoolType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }

        return null;
    }

    public static PoolType getByName(String name) {
        for(int i = 0; i < VALUES_ARRAY.length; ++i) {
            PoolType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }

        return null;
    }

    public static PoolType get(int value) {
        switch(value) {
            case 0:
                return DBCP;
            case 1:
                return C3P0;
            case 2:
                return JNDI;
            case 3:
                return CUSTOM;
            default:
                return null;
        }
    }

    private PoolType(int value, String name, String literal) {
        this.value = value;
        this.name = name;
        this.literal = literal;
    }

    public int getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

    public String getLiteral() {
        return this.literal;
    }

    public String toString() {
        return this.literal;
    }
}
