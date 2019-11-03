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

        Map<String, Properties> databaseProperties = DynamicDataSource.getDatabaseProperties();
        Properties properties = databaseProperties.get(DynamicDataSource.getDataSourceKey());
        if(properties.containsKey("isTenant") && Boolean.valueOf(properties.getProperty("isTenant"))) {
            DatabaseMetaData metaData = connection.getMetaData();
            String databaseType = metaData.getDatabaseProductName();
            String scheme = ((AppOperationContext) OperationContextHolder.getContext()).getTenantScheme();

            if (StringUtils.equalsIgnoreCase(databaseType, "PostgreSQL")) {
                connection.createStatement().executeUpdate("SET search_path TO " + scheme);
            } else if (StringUtils.equalsIgnoreCase(databaseType, "MySQL")) {
                connection.createStatement().executeQuery("use " + scheme);
            } else {
                throw new AkDaoException(databaseType + " 暂不支持租户模式。");
            }

            LOGGER.debug("数据源：{}, 切换到租户：{}", properties.getProperty("id"), scheme);
        }
    }
}
