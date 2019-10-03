package com.yunjian.ak.dao.datasource.impl;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.yunjian.ak.dao.datasource.factory.DataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @Description: C3P0的数据源工厂
 * @Author: yong.sun
 * @Date: 2019/5/22 9:34
 * @Version 1.0
 */
public class C3P0DataSourceFactory implements DataSourceFactory {
    public static final String USER = "username";
    public static final String JDBC_URL = "url";
    public static final String DRIVER_CLASS = "driverClassName";
    public static final String MAX_ACTIVE = "maxActive";
    public static final String PASSWORD = "password";
    public static final String MAX_POOL_SIZE = "maxPoolSize";
    public static final String MIN_POOL_SIZE = "minPoolSize";
    public static final String MAX_STATEMENTS = "maxStatements";
    public static final String ACQUIRE_INCREMENT = "acquireIncrement";
    public static final String MAX_IDLE_TIME = "maxIdleTime";
    public static final String MAX_CONNECTION_AGE = "maxConnectionAge";
    public static final String TEST_CONNECTION_ON_CHECKIN = "testConnectionOnCheckin";
    public static final String TEST_CONNECTION_ON_CHECKOUT = "testConnectionOnCheckout";
    public static final String AUTOMATIC_TEST_TABLE = "automaticTestTable";
    public static final String PREFERRED_TEST_QUERY = "preferredTestQuery";
    public static final String IDLE_CONNECTION_TEST_PERIOD = "idleConnectionTestPeriod";

    public C3P0DataSourceFactory() {
    }

    public DataSource createDataSource(Properties properties) throws Exception {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        String value = null;
        value = properties.getProperty("username");
        if (value != null) {
            cpds.setUser(value);
        }

        value = properties.getProperty("password");
        if (value != null) {
            cpds.setPassword(value);
        }

        value = properties.getProperty("url");
        if (value != null) {
            cpds.setJdbcUrl(value);
        }

        value = properties.getProperty("driverClassName");
        if (value != null) {
            cpds.setDriverClass(value);
        }

        value = properties.getProperty("maxPoolSize");
        if (value != null) {
            cpds.setMaxPoolSize(Integer.parseInt(value));
        } else if (properties.getProperty("maxActive") != null) {
            cpds.setMaxPoolSize(Integer.parseInt(properties.getProperty("maxActive")));
        }

        value = properties.getProperty("minPoolSize");
        if (value != null) {
            cpds.setMinPoolSize(Integer.parseInt(value));
        }

        value = properties.getProperty("maxStatements");
        if (value != null) {
            cpds.setMaxStatements(Integer.parseInt(value));
        }

        value = properties.getProperty("acquireIncrement");
        if (value != null) {
            cpds.setAcquireIncrement(Integer.parseInt(value));
        }

        value = properties.getProperty("maxIdleTime");
        if (value != null) {
            cpds.setMaxIdleTime(Integer.parseInt(value));
        }

        value = properties.getProperty("maxConnectionAge");
        if (value != null) {
            cpds.setMaxConnectionAge(Integer.parseInt(value));
        }

        value = properties.getProperty("automaticTestTable");
        if (value != null) {
            cpds.setAutomaticTestTable(value);
        }

        value = properties.getProperty("preferredTestQuery");
        if (value != null) {
            cpds.setPreferredTestQuery(value);
        }

        value = properties.getProperty("idleConnectionTestPeriod");
        if (value != null) {
            cpds.setIdleConnectionTestPeriod(Integer.parseInt(value));
        }

        return cpds;
    }
}
