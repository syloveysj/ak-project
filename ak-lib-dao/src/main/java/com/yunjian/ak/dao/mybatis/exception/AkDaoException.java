package com.yunjian.ak.dao.mybatis.exception;

import com.yunjian.ak.exception.AkRuntimeException;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 13:42
 * @Version 1.0
 */
public class AkDaoException extends AkRuntimeException {
    private static final long serialVersionUID = -3117031226000623566L;

    public AkDaoException(String msg) {
        super(msg);
    }

    public AkDaoException(String msg, Throwable exception) {
        super(msg, exception);
    }
}
