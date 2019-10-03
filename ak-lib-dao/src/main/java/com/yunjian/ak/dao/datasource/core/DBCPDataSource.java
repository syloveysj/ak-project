package com.yunjian.ak.dao.datasource.core;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/22 11:13
 * @Version 1.0
 */
public interface DBCPDataSource extends DataSource {
    boolean isDefaultAutoCommit();

    void setDefaultAutoCommit(boolean defaultAutoCommit);

    void unsetDefaultAutoCommit();

    boolean isSetDefaultAutoCommit();

    int getInitialSize();

    void setInitialSize(int initialSize);

    void unsetInitialSize();

    boolean isSetInitialSize();

    int getMaxActive();

    void setMaxActive(int maxActive);

    void unsetMaxActive();

    boolean isSetMaxActive();

    int getMaxIdle();

    void setMaxIdle(int maxIdle);

    void unsetMaxIdle();

    boolean isSetMaxIdle();

    int getMinIdle();

    void setMinIdle(int minIdle);

    void unsetMinIdle();

    boolean isSetMinIdle();

    int getMaxWait();

    void setMaxWait(int maxWait);

    void unsetMaxWait();

    boolean isSetMaxWait();

    boolean isPoolPreparedStatements();

    void setPoolPreparedStatements(boolean poolPreparedStatements);

    void unsetPoolPreparedStatements();

    boolean isSetPoolPreparedStatements();

    int getMaxOpenPreparedStatements();

    void setMaxOpenPreparedStatements(int maxOpenPreparedStatements);

    void unsetMaxOpenPreparedStatements();

    boolean isSetMaxOpenPreparedStatements();
}
