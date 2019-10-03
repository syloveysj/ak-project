package com.yunjian.ak.dao.core;

import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 11:43
 * @Version 1.0
 */
public interface PKWrapper {
    Object wrapSingleEntityWithPK(Object o);

    List<Object> wrapListEntityWithPK(List<Object> list);
}

