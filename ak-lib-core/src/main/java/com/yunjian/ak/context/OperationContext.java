package com.yunjian.ak.context;

import java.io.Closeable;
import java.util.Collection;
import java.util.Map;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 13:34
 * @Version 1.0
 */
public interface OperationContext extends Map<String, Object> {
    void addResourceCloseOnClear(Closeable closeable);

    Collection<Closeable> getCloseables();
}
