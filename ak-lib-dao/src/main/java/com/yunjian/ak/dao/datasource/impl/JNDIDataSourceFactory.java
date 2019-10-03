package com.yunjian.ak.dao.datasource.impl;

import com.yunjian.ak.dao.datasource.factory.DataSourceFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * @Description: JNDI的数据源工厂
 * @Author: yong.sun
 * @Date: 2019/5/22 9:34
 * @Version 1.0
 */
public class JNDIDataSourceFactory implements DataSourceFactory {
    public static final String JNDI_NAME = "JNDIName";

    public JNDIDataSourceFactory() {
    }

    public DataSource createDataSource(Properties properties) throws Exception {
        Context ctx = new InitialContext();
        return (DataSource)ctx.lookup(properties.getProperty("JNDIName"));
    }
}
