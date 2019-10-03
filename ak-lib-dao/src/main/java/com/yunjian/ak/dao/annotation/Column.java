package com.yunjian.ak.dao.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/22 11:09
 * @Version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Column {
    String id();

    String std() default "";

    String datatype() default "";

    ColumnType type() default ColumnType.normal;

    boolean association() default false;

    boolean searchable() default false;

    SortType sort() default SortType.none;

    boolean index() default false;
}
