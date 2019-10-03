package com.yunjian.ak.dao.mybatis.dialect;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/23 18:52
 * @Version 1.0
 */
public class PostgreSqlDialect implements Dialect {
    private static final String PAGER_SQL_TEMPLATE = "SELECT * FROM (%s) PAGE_SQL_TEMP limit %d offset %d";

    public PostgreSqlDialect() {
    }

    public String getPagableSQL(String originalSql, String sql, int offset, int limit) {
        return String.format("SELECT * FROM (%s) PAGE_SQL_TEMP limit %d offset %d", sql, limit, offset);
    }

    public boolean isSupport(String databaseType, int databaseVersion) {
        return "PostgreSQL".equals(databaseType);
    }

    public String getToDataSqlStr(String data) {
        return null;
    }

    public String getTotalSqlStr(String originSql) {
        return originSql;
    }
}
