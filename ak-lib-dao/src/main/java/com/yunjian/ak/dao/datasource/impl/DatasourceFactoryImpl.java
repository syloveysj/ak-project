package com.yunjian.ak.dao.datasource.impl;

import com.yunjian.ak.dao.datasource.core.*;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/22 15:24
 * @Version 1.0
 */
public class DatasourceFactoryImpl implements DatasourceFactory {

    public DatasourceFactoryImpl() {
    }

    public C3P0DataSource createC3P0DataSource() {
        C3P0DataSourceImpl c3P0DataSource = new C3P0DataSourceImpl();
        return c3P0DataSource;
    }

    public DataSource createDataSource() {
        DataSourceImpl dataSource = new DataSourceImpl();
        return dataSource;
    }

    public DataSources createDataSources() {
        DataSourcesImpl dataSources = new DataSourcesImpl();
        return dataSources;
    }

    public DBCPDataSource createDBCPDataSource() {
        DBCPDataSourceImpl dbcpDataSource = new DBCPDataSourceImpl();
        return dbcpDataSource;
    }

    public TableInfo createTableInfo() {
        TableInfoImpl tableInfo = new TableInfoImpl();
        return tableInfo;
    }

    public CustomDataSource createCustomDataSource() {
        CustomDataSourceImpl customDataSource = new CustomDataSourceImpl();
        return customDataSource;
    }

    public PoolAttribute createPoolAttribute() {
        PoolAttributeImpl poolAttribute = new PoolAttributeImpl();
        return poolAttribute;
    }

    public JNDIDataSource createJNDIDataSource() {
        JNDIDataSourceImpl jndiDataSource = new JNDIDataSourceImpl();
        return jndiDataSource;
    }

}
