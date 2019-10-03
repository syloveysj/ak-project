package com.yunjian.ak.dao.mybatis.enhance;

import com.yunjian.ak.context.AppOperationContext;
import com.yunjian.ak.context.OperationContextHolder;
import com.yunjian.ak.dao.core.DynamicDataSource;
import com.yunjian.ak.dao.mybatis.exception.AkDaoException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Map;
import java.util.Properties;

/**
 * @Description: 切换连接至租户模式
 * @Author: yong.sun
 * @Date: 2019/6/6 16:22
 * @Version 1.0
 */
public class ConnectionTenantScheme {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionTenantScheme.class);

    public static void change(Connection connection) throws Exception {
        if(!OperationContextHolder.enableTenant()) return;

        DatabaseMetaData metaData = connection.getMetaData();
        String databaseType = metaData.getDatabaseProductName();
        String databaseUrl = metaData.getURL();
        String databaseUserName = metaData.getUserName();

        Map<String, Properties> databaseProperties = DynamicDataSource.getDatabaseProperties();
        for (Map.Entry<String, Properties> entry : databaseProperties.entrySet()) {
            Properties properties = entry.getValue();
            if(properties.containsKey("isTenant") && Boolean.valueOf(properties.getProperty("isTenant"))) {
                if(StringUtils.isEmpty(((AppOperationContext) OperationContextHolder.getContext()).getTenantScheme())) {
                    LOGGER.error("租户ID为空，不能进行数据库操作！");
                    throw new AkDaoException("当前数据源，租户ID不能为空。");
                }

                // 如果当前连接是存在租户配置的数据源（条件是：连接地址+用户账号 相等）
                if (databaseUserName.equals(properties.getProperty("username")) && databaseUrl.equals(properties.getProperty("url"))) {
                    if (StringUtils.equalsIgnoreCase(databaseType, "PostgreSQL")) {
                        connection.createStatement().executeUpdate("SET search_path TO " + ((AppOperationContext) OperationContextHolder.getContext()).getTenantScheme());
                    } else if (StringUtils.equalsIgnoreCase(databaseType, "MySQL")) {
                        connection.createStatement().executeQuery("use " + ((AppOperationContext) OperationContextHolder.getContext()).getTenantScheme());
                    } else {
                        throw new AkDaoException(databaseType + " 暂不支持租户模式。");
                    }
                }
            }
        }
    }
}
