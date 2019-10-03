package com.yunjian.ak.dao.mybatis.exception;

import java.sql.SQLException;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 13:41
 * @Version 1.0
 */
public class AkDaoExceptionTranslator {
    public AkDaoExceptionTranslator() {
    }

    public static AkDaoException doTranslate(String task, Exception e, String dbName) {
        AkDaoException daoEx = null;
        if (e.getCause() instanceof SQLException) {
            String errorCode = ((SQLException)e.getCause()).getErrorCode() + "";
            SQLErrorNums sen = SQLErrorNumsFactory.getInstance().getErrorNums(dbName);
            if (sen != null && sen.getSqlErrorNums().containsKey(errorCode)) {
                daoEx = new AkSQLException(task + "SQL异常!", e);
                ((AkDaoException)daoEx).setErrorNum((String)sen.getSqlErrorNums().get(errorCode));
            } else {
                daoEx = new AkSQLException(task + (e.getCause() == null ? e.getMessage() : e.getCause().getMessage()), e);
            }
        } else {
            daoEx = new AkDaoException(task + (e.getCause() == null ? e.getMessage() : e.getCause().getMessage()), e);
        }

        return (AkDaoException)daoEx;
    }
}
