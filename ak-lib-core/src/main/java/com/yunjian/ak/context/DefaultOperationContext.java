package com.yunjian.ak.context;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 13:34
 * @Version 1.0
 */
public class DefaultOperationContext extends HashMap<String, Object> implements OperationContext {
    private static final long serialVersionUID = -4941823564172696313L;
    private Collection<Closeable> closeables = new ArrayList();

    protected DefaultOperationContext() {
    }

    public void addResourceCloseOnClear(Closeable closeable) {
        this.closeables.add(closeable);
    }

    public Collection<Closeable> getCloseables() {
        return this.closeables;
    }
}
