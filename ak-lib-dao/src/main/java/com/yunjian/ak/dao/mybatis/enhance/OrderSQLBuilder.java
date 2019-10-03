package com.yunjian.ak.dao.mybatis.enhance;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.Iterator;
import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 10:20
 * @Version 1.0
 */
public class OrderSQLBuilder {
    private static final String SORT_SQL_TEMPLATE = "SELECT * FROM (%s) sort_sql_temp ORDER BY%s";

    public OrderSQLBuilder() {
    }

    public static String build(String originalSQL, MappedStatement mappedStatement, Sortable sortable) {
        Preconditions.checkArgument(originalSQL != null, "参数originalSQL不能为null");
        Preconditions.checkArgument(mappedStatement != null, "参数mappedStatement不能为null");
        Preconditions.checkArgument(sortable != null, "参数sortable不能为null");
        List<Order> orders = sortable.getOrders();
        if (orders != null && orders.size() != 0) {
            StringBuilder orderBySql = new StringBuilder();
            Iterator iterator = orders.iterator();

            while(iterator.hasNext()) {
                Order order = (Order)iterator.next();
                if (order != null) {
                    String property = order.getOrderProperty();
                    String direction = order.getOrderDirection();
                    if (!Strings.isNullOrEmpty(property) && !Strings.isNullOrEmpty(direction)) {
                        String column = MybatisExtractor.extractColumn(property, mappedStatement);
                        Preconditions.checkArgument(column != null, "属性" + property + "在resultMap中找不到对应的字段映射!");
                        orderBySql.append(" ").append(column).append(" ").append(direction).append(",");
                    }
                }
            }

            if (orderBySql.length() > 0) {
                int index = orderBySql.lastIndexOf(",");
                if (index == orderBySql.length() - 1) {
                    orderBySql.deleteCharAt(index);
                }

                return String.format("SELECT * FROM (%s) sort_sql_temp ORDER BY%s", originalSQL, orderBySql.toString());
            } else {
                return originalSQL;
            }
        } else {
            return originalSQL;
        }
    }
}
