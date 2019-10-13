package com.yunjian.ak.result;

import com.alibaba.fastjson.JSONObject;
import com.yunjian.ak.exception.AkManagedException;
import com.yunjian.ak.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 统一异常处理
 * @Author: yong.sun
 * @Date: 2019/10/3 23:59
 * @Version 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 验证异常
     * @param request
     * @param e
     * @return
     * @throws MethodArgumentNotValidException
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public Object handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e, HttpServletResponse response) throws MethodArgumentNotValidException {
        ApiResult result = ApiResult.errorOf(ErrorCode.PARAMS_ERROR);
        BindingResult bindingResult = e.getBindingResult();
        String errorMesssage = "无效请求:\n";

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMesssage += fieldError.getDefaultMessage() + "\n";
        }
        result.setMessage(errorMesssage);
        logger.info("MethodArgumentNotValidException", e);
//        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return result;
    }

    /**
     * 全局异常
     * @param request
     * @param e
     * @param response
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object logicExceptionHandler(HttpServletRequest request, Exception e, HttpServletResponse response) {
        //系统级异常，错误码固定为-1，提示语固定为系统繁忙，请稍后再试
        ApiResult result = ApiResult.errorOf(ErrorCode.SERVER_ERROR);
        //如果是业务逻辑异常，返回具体的错误码与提示信息
        if (e instanceof AkManagedException) {
            AkManagedException akManagedException = (AkManagedException) e;
            result.setCode(ErrorCode.SERVER_ERROR.getCode());
            result.setMessage(akManagedException.getMessage());
        } else if (e instanceof ValidationException) {
            ValidationException validationException = (ValidationException) e;
            result.setCode(ErrorCode.PARAMS_ERROR.getCode());
            result.setMessage(validationException.getMessage());
        } else {
            //对系统级异常进行日志记录
            logger.error("系统异常:" + e.getMessage(), e);
        }
//        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return JSONObject.toJSON(result);
    }
}
