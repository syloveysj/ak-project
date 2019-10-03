package com.yunjian.ak.dao.datasource.impl;

import com.yunjian.ak.dao.datasource.factory.DataSourceFactory;
import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @Description: DBCP的数据源工厂
 * @Author: yong.sun
 * @Date: 2019/5/22 9:34
 * @Version 1.0
 */
public class DBCPDataSourceFactory implements DataSourceFactory {
    public DBCPDataSourceFactory() {
    }

    public DataSource createDataSource(Properties properties) throws Exception {
        return BasicDataSourceFactory.createDataSource(properties);
    }
}
