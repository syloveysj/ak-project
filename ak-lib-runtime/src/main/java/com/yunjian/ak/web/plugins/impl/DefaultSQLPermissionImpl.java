package com.yunjian.ak.web.plugins.impl;

import com.yunjian.ak.plugins.SQLPermissionPlugin;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @Description: SQL权限表达式提取
 * @Author: yong.sun
 * @Date: 2019/7/1 15:50
 * @Version 1.0
 */
public class DefaultSQLPermissionImpl implements SQLPermissionPlugin {
    protected final Log logger = LogFactory.getLog(this.getClass());

    public String getExpr(String mappedId) {
        return null;
    }
}
