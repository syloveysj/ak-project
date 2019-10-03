package com.yunjian.ak.exception;

/**
 * @Description: 运行时异常
 * @Author: yong.sun
 * @Date: 2019/5/22 9:34
 * @Version 1.0
 */
public abstract class AkRuntimeException extends RuntimeException implements AkManagedException {
    private static final long serialVersionUID = 1974160829981291868L;
    private String errNum = DEFAULT_ERROR_NUM;

    public AkRuntimeException(String message) {
        super(message);
    }

    public AkRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getErrorNum() {
        return this.errNum;
    }

    public AkRuntimeException setErrorNum(String errorNum) {
        this.errNum = errorNum;
        return this;
    }
}
