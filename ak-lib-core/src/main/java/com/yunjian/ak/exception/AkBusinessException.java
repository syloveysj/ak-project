package com.yunjian.ak.exception;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/1 22:20
 * @Version 1.0
 */
public interface AkBusinessException {
    String DEFAULT_ERROR_NUM = "unkown exception";

    String getErrorNum();

    AkManagedException setErrorNum(String errorNum);

    String getMessage();

    Throwable getCause();
}
