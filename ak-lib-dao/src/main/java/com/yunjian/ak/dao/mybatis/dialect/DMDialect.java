package com.yunjian.ak.dao.mybatis.dialect;

import com.yunjian.ak.utils.StringUtil;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/23 18:51
 * @Version 1.0
 */
public class DMDialect implements Dialect {
    private static final String PAGER_SQL_TEMPLATE = "select * from (select temp.*, rownum rn from (%s) temp where rownum<=%d) where rn>%d";

    public DMDialect() {
    }

    public String getPagableSQL(String originalSql, String sql, int offset, int limit) {
        return String.format("select * from (select temp.*, rownum rn from (%s) temp where rownum<=%d) where rn>%d", sql, limit + offset, offset);
    }

    public boolean isSupport(String databaseType, int databaseVersion) {
        return "DM DBMS".equals(databaseType);
    }

    public String getToDataSqlStr(String data) {
        String toDateFormater = "yyyy-mm-dd";
        String toTimeFormater = "hh24:mi:ss";
        int colon;
        if (data.matches("\\d{4}(-(0\\d{1}|1[0-2]))?(-(0\\d{1}|[12]\\d{1}|3[01]))?(\\s+)?(0\\d{1}|1\\d{1}|2[0-3])?(:[0-5]\\d{1})?(:([0-5]\\d{1}))?")) {
            colon = data.split("-").length - 1;
            if (colon < 2) {
                toDateFormater = StringUtil.getMatchedStr(toDateFormater, "^\\w+-{" + colon + "}\\w+", 2);
            }
        } else {
            toDateFormater = "";
        }

        if (data.matches("\\d{4}-(0\\d{1}|1[0-2])-(0\\d{1}|[12]\\d{1}|3[01])\\s+(0\\d{1}|1\\d{1}|2[0-3])(:[0-5]\\d{1})?(:([0-5]\\d{1}))?")) {
            colon = data.split(":").length - 1;
            if (colon < 2) {
                toTimeFormater = StringUtil.getMatchedStr(toTimeFormater, "^\\w+:{" + colon + "}\\w+", 2);
            }
        } else {
            toTimeFormater = "";
        }

        return "to_date('" + data + "','" + toDateFormater + " " + toTimeFormater + "')";
    }

    public String getTotalSqlStr(String originSql) {
        return originSql;
    }
}
