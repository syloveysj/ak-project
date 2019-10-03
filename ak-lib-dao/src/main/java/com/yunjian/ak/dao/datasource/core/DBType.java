package com.yunjian.ak.dao.datasource.core;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Description: 数据库类型枚举
 * @Author: yong.sun
 * @Date: 2019/5/22 9:34
 * @Version 1.0
 */
public enum DBType {
    MY_SQL(0, "MySQL", "MySQL"),
    ORACLE(1, "Oracle", "Oracle"),
    SQL_SERVER(2, "SQLServer", "SQL Server"),
    DB2(3, "DB2", "DB2"),
    POSTGRE_SQL(4, "PostgreSQL", "PostgreSQL"),
    CUSTOM_DB(5, "CustomDB", "CustomDB");

    public static final int MY_SQL_VALUE = 0;
    public static final int ORACLE_VALUE = 1;
    public static final int SQL_SERVER_VALUE = 2;
    public static final int DB2_VALUE = 3;
    public static final int POSTGRE_SQL_VALUE = 4;
    public static final int CUSTOM_DB_VALUE = 5;
    private static final DBType[] VALUES_ARRAY = new DBType[]{MY_SQL, ORACLE, SQL_SERVER, DB2, POSTGRE_SQL, CUSTOM_DB};
    public static final List<DBType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));
    private final int value;
    private final String name;
    private final String literal;

    public static DBType get(String literal) {
        for(int i = 0; i < VALUES_ARRAY.length; ++i) {
            DBType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }

        return null;
    }

    public static DBType getByName(String name) {
        for(int i = 0; i < VALUES_ARRAY.length; ++i) {
            DBType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }

        return null;
    }

    public static DBType get(int value) {
        switch(value) {
            case 0:
                return MY_SQL;
            case 1:
                return ORACLE;
            case 2:
                return SQL_SERVER;
            case 3:
                return DB2;
            case 4:
                return POSTGRE_SQL;
            case 5:
                return CUSTOM_DB;
            default:
                return null;
        }
    }

    private DBType(int value, String name, String literal) {
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
