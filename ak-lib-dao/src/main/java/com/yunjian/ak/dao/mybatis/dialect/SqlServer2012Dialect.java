package com.yunjian.ak.dao.mybatis.dialect;

import com.google.common.collect.Lists;
import com.yunjian.ak.dao.mybatis.exception.FrameworkSQLException;
import com.yunjian.ak.utils.StringUtil;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/23 18:52
 * @Version 1.0
 */
public class SqlServer2012Dialect implements Dialect {
    private static final String PAGER_SQL_TEMPLATE = "%s offset %d rows fetch next %d rows only";
    private static final String PAGER_SQL_NEST_TEMPLATE = "select * from (%s) PAGE_SQL_TEMP ORDER BY CURRENT_TIMESTAMP offset %d rows fetch next %d rows only";

    public SqlServer2012Dialect() {
    }

    public String getPagableSQL(String originalSql, String sql, int offset, int limit) {
        originalSql = originalSql.toUpperCase(Locale.ENGLISH).replace('\n', ' ');
        if (originalSql.matches(".*\\s+ORDER\\s+BY.*")) {
            throw new FrameworkSQLException("由于SqlServer不支持嵌套子查询中的排序子句,请将Mapper中分页sql中的OrderBY子句删除,用Dao接口中的Sortable入参实现.");
        } else {
            return sql.contains("sort_sql_temp") ? String.format("%s offset %d rows fetch next %d rows only", sql, offset, limit) : String.format("select * from (%s) PAGE_SQL_TEMP ORDER BY CURRENT_TIMESTAMP offset %d rows fetch next %d rows only", sql, offset, limit);
        }
    }

    public boolean isSupport(String databaseType, int databaseVersion) {
        return "Microsoft SQL Server".equals(databaseType) && databaseVersion >= 11;
    }

    public String getToDataSqlStr(String data) {
        String toDateFormater = "yyyy-mm-dd";
        String toTimeFormater = " hh:mi:ss";
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
                toTimeFormater = StringUtil.getMatchedStr(toTimeFormater, "^\\s\\w+:{" + colon + "}\\w+", 2);
            }
        } else {
            toTimeFormater = "";
        }

        return "CONVERT(varchar(" + (toDateFormater.length() + toTimeFormater.length()) + "),'" + data + "',120)";
    }

    public String getTotalSqlStr(String originSql) {
        String totalSql = originSql.toUpperCase(Locale.ENGLISH).replace('\n', ' ');
        Matcher matcher = Pattern.compile("\\s+ORDER\\s+BY\\s+[^\\)]*\\)?").matcher(totalSql);
        StringBuffer sb = new StringBuffer();

        while(matcher.find()) {
            if (matcher.group().endsWith(")")) {
                matcher.appendReplacement(sb, " ) ");
            } else {
                matcher.appendReplacement(sb, " ");
            }
        }

        matcher.appendTail(sb);
        if (sb.length() != 0) {
            return sb.toString();
        } else {
            return totalSql;
        }
    }

    public static void main(String[] args) {
        List<String> list = Lists.newArrayList(new String[]{"2015", "2015-01", "2015-01-01", "2015-01-01 23", "2015-01-01 23:23", "2015-01-01 23:23:23"});
        SqlServer2012Dialect dialect = new SqlServer2012Dialect();
        Iterator iterator = list.iterator();

        while(iterator.hasNext()) {
            String data = (String)iterator.next();
            String data1 = dialect.getToDataSqlStr(data);
            Assert.isTrue(data1.equals("CONVERT(varchar(" + data.length() + "),'" + data + "',120)"), data + "转换错误...");
        }

    }
}
