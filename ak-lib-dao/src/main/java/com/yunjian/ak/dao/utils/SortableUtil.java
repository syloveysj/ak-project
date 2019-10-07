package com.yunjian.ak.dao.utils;

import com.yunjian.ak.dao.mybatis.enhance.Order;
import com.yunjian.ak.dao.mybatis.enhance.Sortable;
import com.yunjian.ak.dao.mybatis.exception.InvalidQueryException;
import org.apache.commons.lang.StringUtils;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/30 15:35
 * @Version 1.0
 */
public class SortableUtil {
    public SortableUtil() {
    }

    public static Sortable getSortable(String sidx, String sord) {
        Sortable sortable = null;
        if (StringUtils.isNotEmpty(sidx) && StringUtils.isNotEmpty(sord)) {
            String sortStr = sidx + " " + sord;
            String[] pairs = sortStr.split(",");
            Order[] orders = new Order[pairs.length];

            for(int i = 0; i < pairs.length; ++i) {
                String[] order = pairs[i].trim().split("\\s+");
                if (order.length < 2) {
                    throw new InvalidQueryException("排序参数格式错误：" + sortStr);
                }

                orders[i] = new Order(order[0].trim(), order[1].trim());
            }

            if (orders.length > 0) {
                sortable = new Sortable(orders);
            }
        }

        return sortable;
    }
}
