package com.yunjian.ak.dao.mybatis.dialect;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/23 18:51
 * @Version 1.0
 */
public class DB2Dialect implements Dialect {
    public DB2Dialect() {
    }

    public String getPagableSQL(String originalSql, String sql, int offset, int limit) {
        return null;
    }

    public boolean isSupport(String databaseType, int databaseVersion) {
        return false;
    }

    public String getToDataSqlStr(String data) {
        return null;
    }

    public String getTotalSqlStr(String originSql) {
        return null;
    }
}
