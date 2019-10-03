package com.yunjian.ak.dao.mybatis.exception;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/23 18:55
 * @Version 1.0
 */
public class FrameworkSQLException extends RuntimeException {
    private static final long serialVersionUID = -6336891869793793168L;

    public FrameworkSQLException(String message) {
        super(message);
    }

    public FrameworkSQLException(String message, Throwable t) {
        super(message, t);
    }
}
