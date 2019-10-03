package com.yunjian.ak.exception;

/**
 * @Description: 服务器配置异常
 * @Author: yong.sun
 * @Date: 2019/5/22 9:34
 * @Version 1.0
 */
public class ServerConfigException extends AkRuntimeException {
    private static final long serialVersionUID = -7574848678097167591L;

    public ServerConfigException(String message) {
        super(message);
    }
}
