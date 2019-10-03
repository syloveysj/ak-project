package com.yunjian.ak.dao.mybatis.enhance;

import com.google.common.base.Preconditions;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 10:20
 * @Version 1.0
 */
public class DynamicQuerySQLParser {
    public DynamicQuerySQLParser() {
    }

    public static String parse(String originalSQL, MappedStatement mappedStatement) {
        Preconditions.checkArgument(originalSQL != null, "参数originalSQL不能为null");
        Preconditions.checkArgument(mappedStatement != null, "参数mappedStatement不能为null");
        Matcher matcher = Pattern.compile("(\\{[^\\}]+?\\})").matcher(originalSQL);
        StringBuffer sql = new StringBuffer();

        while(matcher.find()) {
            String parameter = matcher.group(1);
            String property = parameter.substring(1, parameter.length() - 1);
            String column = MybatisExtractor.extractColumn(property, mappedStatement);
            Preconditions.checkArgument(column != null, "属性" + property + "在resultMap中找不到对应的字段映射!");
            matcher.appendReplacement(sql, Matcher.quoteReplacement(column));
        }

        return matcher.appendTail(sql).toString();
    }
}
