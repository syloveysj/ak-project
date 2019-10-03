package com.yunjian.ak.dao.annotation;

import java.lang.annotation.*;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/22 11:06
 * @Version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface Entity {
    String id();

    String table();

    String ds() default "";

    boolean log() default false;

    boolean cache() default true;

    boolean autoIndex() default true;

    boolean logicDelete() default false;
}
