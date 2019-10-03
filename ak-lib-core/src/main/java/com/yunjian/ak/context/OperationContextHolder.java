package com.yunjian.ak.context;

import com.yunjian.ak.config.ConfigManager;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 13:33
 * @Version 1.0
 */
public class OperationContextHolder {
    private static final Logger LOGGER = LoggerFactory.getLogger(OperationContextHolder.class);
    private static final ThreadLocal<OperationContext> local = new ThreadLocal();

    public OperationContextHolder() {
    }

    public static OperationContext getContext() {
        OperationContext context = (OperationContext)local.get();
        if (context == null) {
//            context = new DefaultWebOperationContext();
            context = new DefaultAppOperationContext();
            local.set(context);
        }

        return (OperationContext)context;
    }

    public static boolean enableTenant () {
        return StringUtils.equalsIgnoreCase("true", ConfigManager.getInstance().getConfig("ak_dao_enable_tenant_mode"));
    }

    public static void clear() {
        OperationContext context = (OperationContext)local.get();
        if (context != null) {
            Collection<Closeable> closeables = context.getCloseables();
            if (closeables != null) {
                Iterator iterator = closeables.iterator();

                while(iterator.hasNext()) {
                    Closeable closeable = (Closeable)iterator.next();

                    try {
                        closeable.close();
                    } catch (IOException e) {
                        LOGGER.warn("关闭操作上下文中的Closeable资源时发生错误:{}", e.getMessage());
                    }
                }
            }
        }

        local.remove();
    }
}
