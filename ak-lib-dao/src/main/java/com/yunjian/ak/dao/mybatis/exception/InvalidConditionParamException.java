package com.yunjian.ak.dao.mybatis.exception;

import com.yunjian.ak.exception.AkRuntimeException;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 10:34
 * @Version 1.0
 */
public class InvalidConditionParamException extends AkRuntimeException {
    private static final long serialVersionUID = -8344660487155791375L;

    public InvalidConditionParamException(String msg) {
        super(msg);
    }

    public InvalidConditionParamException(String msg, Throwable exception) {
        super(msg, exception);
    }
}
