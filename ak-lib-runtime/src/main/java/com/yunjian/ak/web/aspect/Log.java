package com.yunjian.ak.web.aspect;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/11/14 23:58
 * @Version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Log {
    /**
     * 日志描述，这里使用了@AliasFor 别名。spring提供的
     * @return
     */
    @AliasFor("desc")    String value() default "";

    /**
     * 日志描述
     * @return
     */
    @AliasFor("value")    String desc() default "";

    /**
     * 是否不记录日志
     * @return
     */
    boolean ignore() default false;
}
