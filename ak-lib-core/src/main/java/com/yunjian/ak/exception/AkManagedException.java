package com.yunjian.ak.exception;

/**
 * @Description: 业务类异常的接口
 * @Author: yong.sun
 * @Date: 2019/5/22 9:34
 * @Version 1.0
 */
public interface AkManagedException {
    String DEFAULT_ERROR_NUM = "unkown exception";

    String getErrorNum();

    AkManagedException setErrorNum(String errorNum);

    String getMessage();

    Throwable getCause();
}
