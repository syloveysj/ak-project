package com.yunjian.ak.dao.datasource.impl;

import com.yunjian.ak.dao.datasource.core.DBCPDataSource;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/22 15:15
 * @Version 1.0
 */
public class DBCPDataSourceImpl extends DataSourceImpl implements DBCPDataSource {
    protected static final boolean DEFAULT_AUTO_COMMIT_EDEFAULT = false;
    protected boolean defaultAutoCommit = false;
    protected boolean defaultAutoCommitESet;
    protected static final int INITIAL_SIZE_EDEFAULT = 0;
    protected int initialSize = 0;
    protected boolean initialSizeESet;
    protected static final int MAX_ACTIVE_EDEFAULT = 0;
    protected int maxActive = 0;
    protected boolean maxActiveESet;
    protected static final int MAX_IDLE_EDEFAULT = 0;
    protected int maxIdle = 0;
    protected boolean maxIdleESet;
    protected static final int MIN_IDLE_EDEFAULT = 0;
    protected int minIdle = 0;
    protected boolean minIdleESet;
    protected static final int MAX_WAIT_EDEFAULT = 0;
    protected int maxWait = 0;
    protected boolean maxWaitESet;
    protected static final boolean POOL_PREPARED_STATEMENTS_EDEFAULT = false;
    protected boolean poolPreparedStatements = false;
    protected boolean poolPreparedStatementsESet;
    protected static final int MAX_OPEN_PREPARED_STATEMENTS_EDEFAULT = 0;
    protected int maxOpenPreparedStatements = 0;
    protected boolean maxOpenPreparedStatementsESet;

    protected DBCPDataSourceImpl() {
    }

    public boolean isDefaultAutoCommit() {
        return this.defaultAutoCommit;
    }

    public void setDefaultAutoCommit(boolean newDefaultAutoCommit) {
        this.defaultAutoCommit = newDefaultAutoCommit;
        this.defaultAutoCommitESet = true;
    }

    public void unsetDefaultAutoCommit() {
        this.defaultAutoCommit = false;
        this.defaultAutoCommitESet = false;
    }

    public boolean isSetDefaultAutoCommit() {
        return this.defaultAutoCommitESet;
    }

    public int getInitialSize() {
        return this.initialSize;
    }

    public void setInitialSize(int newInitialSize) {
        this.initialSize = newInitialSize;
        this.initialSizeESet = true;
    }

    public void unsetInitialSize() {
        this.initialSize = 0;
        this.initialSizeESet = false;
    }

    public boolean isSetInitialSize() {
        return this.initialSizeESet;
    }

    public int getMaxActive() {
        return this.maxActive;
    }

    public void setMaxActive(int newMaxActive) {
        this.maxActive = newMaxActive;
        this.maxActiveESet = true;
    }

    public void unsetMaxActive() {
        this.maxActive = 0;
        this.maxActiveESet = false;
    }

    public boolean isSetMaxActive() {
        return this.maxActiveESet;
    }

    public int getMaxIdle() {
        return this.maxIdle;
    }

    public void setMaxIdle(int newMaxIdle) {
        this.maxIdle = newMaxIdle;
        this.maxIdleESet = true;
    }

    public void unsetMaxIdle() {
        this.maxIdle = 0;
        this.maxIdleESet = false;
    }

    public boolean isSetMaxIdle() {
        return this.maxIdleESet;
    }

    public int getMinIdle() {
        return this.minIdle;
    }

    public void setMinIdle(int newMinIdle) {
        this.minIdle = newMinIdle;
        this.minIdleESet = true;
    }

    public void unsetMinIdle() {
        this.minIdle = 0;
        this.minIdleESet = false;
    }

    public boolean isSetMinIdle() {
        return this.minIdleESet;
    }

    public int getMaxWait() {
        return this.maxWait;
    }

    public void setMaxWait(int newMaxWait) {
        this.maxWait = newMaxWait;
        this.maxWaitESet = true;
    }

    public void unsetMaxWait() {
        this.maxWait = 0;
        this.maxWaitESet = false;
    }

    public boolean isSetMaxWait() {
        return this.maxWaitESet;
    }

    public boolean isPoolPreparedStatements() {
        return this.poolPreparedStatements;
    }

    public void setPoolPreparedStatements(boolean newPoolPreparedStatements) {
        this.poolPreparedStatements = newPoolPreparedStatements;
        this.poolPreparedStatementsESet = true;
    }

    public void unsetPoolPreparedStatements() {
        this.poolPreparedStatements = false;
        this.poolPreparedStatementsESet = false;
    }

    public boolean isSetPoolPreparedStatements() {
        return this.poolPreparedStatementsESet;
    }

    public int getMaxOpenPreparedStatements() {
        return this.maxOpenPreparedStatements;
    }

    public void setMaxOpenPreparedStatements(int newMaxOpenPreparedStatements) {
        this.maxOpenPreparedStatements = newMaxOpenPreparedStatements;
        this.maxOpenPreparedStatementsESet = true;
    }

    public void unsetMaxOpenPreparedStatements() {
        this.maxOpenPreparedStatements = 0;
        this.maxOpenPreparedStatementsESet = false;
    }

    public boolean isSetMaxOpenPreparedStatements() {
        return this.maxOpenPreparedStatementsESet;
    }

    public String toString() {
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (defaultAutoCommit: ");
        if (this.defaultAutoCommitESet) {
            result.append(this.defaultAutoCommit);
        } else {
            result.append("<unset>");
        }

        result.append(", initialSize: ");
        if (this.initialSizeESet) {
            result.append(this.initialSize);
        } else {
            result.append("<unset>");
        }

        result.append(", maxActive: ");
        if (this.maxActiveESet) {
            result.append(this.maxActive);
        } else {
            result.append("<unset>");
        }

        result.append(", maxIdle: ");
        if (this.maxIdleESet) {
            result.append(this.maxIdle);
        } else {
            result.append("<unset>");
        }

        result.append(", minIdle: ");
        if (this.minIdleESet) {
            result.append(this.minIdle);
        } else {
            result.append("<unset>");
        }

        result.append(", maxWait: ");
        if (this.maxWaitESet) {
            result.append(this.maxWait);
        } else {
            result.append("<unset>");
        }

        result.append(", poolPreparedStatements: ");
        if (this.poolPreparedStatementsESet) {
            result.append(this.poolPreparedStatements);
        } else {
            result.append("<unset>");
        }

        result.append(", maxOpenPreparedStatements: ");
        if (this.maxOpenPreparedStatementsESet) {
            result.append(this.maxOpenPreparedStatements);
        } else {
            result.append("<unset>");
        }

        result.append(')');
        return result.toString();
    }
}
