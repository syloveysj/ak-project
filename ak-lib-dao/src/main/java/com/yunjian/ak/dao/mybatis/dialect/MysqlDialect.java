package com.yunjian.ak.dao.mybatis.dialect;

import com.yunjian.ak.utils.StringUtil;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/23 18:52
 * @Version 1.0
 */
public class MysqlDialect implements Dialect {
    private static final String PAGER_SQL_TEMPLATE = "%s limit %d, %d";

    public MysqlDialect() {
    }

    public String getPagableSQL(String originalSql, String sql, int offset, int limit) {
        return String.format("%s limit %d, %d", sql, offset, limit);
    }

    public boolean isSupport(String databaseType, int databaseVersion) {
        return "MySQL".equals(databaseType);
    }

    public String getToDataSqlStr(String data) {
        String toDateFormater = "%Y-%m-%d";
        String toTimeFormater = "%H:%i:%s";
        int colon;
        if (data.matches("\\d{4}(-(0\\d{1}|1[0-2]))?(-(0\\d{1}|[12]\\d{1}|3[01]))?(\\s+)?(0\\d{1}|1\\d{1}|2[0-3])?(:[0-5]\\d{1})?(:([0-5]\\d{1}))?")) {
            colon = data.split("-").length - 1;
            if (colon < 2) {
                toDateFormater = StringUtil.getMatchedStr(toDateFormater, "^[^-]+-{" + colon + "}[^-]+", 2);
            }
        } else {
            toDateFormater = "";
        }

        if (data.matches("\\d{4}-(0\\d{1}|1[0-2])-(0\\d{1}|[12]\\d{1}|3[01])\\s+(0\\d{1}|1\\d{1}|2[0-3])(:[0-5]\\d{1})?(:([0-5]\\d{1}))?")) {
            colon = data.split(":").length - 1;
            if (colon < 2) {
                toTimeFormater = StringUtil.getMatchedStr(toTimeFormater, "^[^:]+:{" + colon + "}[^:]+", 2);
            }
        } else {
            toTimeFormater = "";
        }

        return "str_to_date('" + data + "','" + toDateFormater + " " + toTimeFormater + "')";
    }

    public String getTotalSqlStr(String originSql) {
        return originSql;
    }
}
