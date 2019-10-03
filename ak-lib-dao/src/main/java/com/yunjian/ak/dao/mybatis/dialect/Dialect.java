package com.yunjian.ak.dao.mybatis.dialect;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/23 18:47
 * @Version 1.0
 */
public interface Dialect {
    String getPagableSQL(String originalSql, String sql, int offset, int limit);

    boolean isSupport(String databaseType, int databaseVersion);

    String getToDataSqlStr(String data);

    String getTotalSqlStr(String originSql);
}