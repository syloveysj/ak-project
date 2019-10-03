package com.yunjian.ak.id;

import com.yunjian.ak.exception.AkRuntimeException;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 12:15
 * @Version 1.0
 */
public class IDGenneratorNotInitException extends AkRuntimeException {
    private static final long serialVersionUID = 1065653039666124587L;

    public IDGenneratorNotInitException() {
        super("IDGenerator没有正确的被初始化!");
    }
}
