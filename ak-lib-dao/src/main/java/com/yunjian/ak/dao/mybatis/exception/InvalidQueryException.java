package com.yunjian.ak.dao.mybatis.exception;

import com.yunjian.ak.exception.AkRuntimeException;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/30 15:36
 * @Version 1.0
 */
public class InvalidQueryException extends AkRuntimeException {
    private static final long serialVersionUID = 3322301631645995838L;

    public InvalidQueryException(String message) {
        super(message);
    }
}
