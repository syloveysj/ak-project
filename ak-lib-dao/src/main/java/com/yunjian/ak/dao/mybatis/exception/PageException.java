package com.yunjian.ak.dao.mybatis.exception;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 10:25
 * @Version 1.0
 */
public class PageException extends FrameworkSQLException {
    private static final long serialVersionUID = 4647174395253872998L;

    public PageException(String message) {
        super(message);
    }

    public PageException(String message, Throwable t) {
        super(message, t);
    }
}

