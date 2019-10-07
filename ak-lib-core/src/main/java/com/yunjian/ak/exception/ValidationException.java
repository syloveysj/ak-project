package com.yunjian.ak.exception;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/5 22:13
 * @Version 1.0
 */
public class ValidationException extends RuntimeException implements AkBusinessException {
    private String errNum = DEFAULT_ERROR_NUM;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getErrorNum() {
        return this.errNum;
    }

    public ValidationException setErrorNum(String errorNum) {
        this.errNum = errorNum;
        return this;
    }
}
