package com.yunjian.ak.result;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/3 23:37
 * @Version 1.0
 */
@Data
@AllArgsConstructor
public class ApiResult {
    /**
     * 错误码，对应{@link ErrorCode}，表示一种错误类型
     * 如果是成功，则code为200
     */
    private int code;
    /**
     * 对错误的具体解释
     */
    private String message;
    /**
     * 返回的结果包装在data中，data可以是单个对象
     */
    private final Object data;

    public static ApiResult dataOf(Object data) {
        return new ApiResult(ErrorCode.SUCCESS.getCode(), null, data);
    }

    public static ApiResult errorOf(ErrorCode errorCode) {
        return new ApiResult(errorCode.getCode(), errorCode.getMsg(), null);
    }
    public static ApiResult errorOf(ErrorCode errorCode, String message) {
        return new ApiResult(errorCode.getCode(), message, null);
    }
}
