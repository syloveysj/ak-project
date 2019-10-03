package com.yunjian.ak.dao.mybatis.exception;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 13:45
 * @Version 1.0
 */
public class AkSQLException extends AkDaoException {
    private static final long serialVersionUID = -6972796383222058272L;

    public AkSQLException(String msg) {
        super(msg);
    }

    public AkSQLException(String msg, Throwable exception) {
        super(msg, exception);
    }
}
