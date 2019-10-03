package com.yunjian.ak.dao.datasource.core;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/22 15:22
 * @Version 1.0
 */
public interface DatasourceFactory {
    C3P0DataSource createC3P0DataSource();

    DataSource createDataSource();

    DataSources createDataSources();

    DBCPDataSource createDBCPDataSource();

    TableInfo createTableInfo();

    CustomDataSource createCustomDataSource();

    PoolAttribute createPoolAttribute();

    JNDIDataSource createJNDIDataSource();
}
