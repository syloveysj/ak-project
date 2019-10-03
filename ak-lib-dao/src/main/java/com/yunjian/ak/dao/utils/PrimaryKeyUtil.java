package com.yunjian.ak.dao.utils;

import com.yunjian.ak.dao.annotation.Children;
import com.yunjian.ak.dao.annotation.Column;
import com.yunjian.ak.dao.annotation.ColumnType;
import com.yunjian.ak.utils.UUIDUtil;

import java.lang.reflect.Field;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 11:45
 * @Version 1.0
 */
public class PrimaryKeyUtil {
    public PrimaryKeyUtil() {
    }

    public static String setPKWithUUID(Object entity) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException {
        Field pkField = getPK(entity.getClass());
        if (pkField == null) {
            throw new RuntimeException("实体:" + entity + "找不到主键字段");
        } else {
            String uuid = UUIDUtil.createUUID();
            pkField.setAccessible(true);
            pkField.set(entity, uuid);
            return uuid;
        }
    }

    public static void setPKWithFieldName(Object entity, String pkFieldName, Object value) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        Field pkField = getFieldByName(entity.getClass(), pkFieldName);
        if (!pkField.isAccessible()) {
            pkField.setAccessible(true);
        }

        pkField.set(entity, getValue(pkField, value));
    }

    public static void setPK(Object entity, Object value) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException {
        Field pkField = getPK(entity.getClass());
        if (pkField == null) {
            throw new RuntimeException("实体:" + entity + "找不到主键字段");
        } else {
            pkField.setAccessible(true);
            pkField.set(entity, getValue(pkField, value));
        }
    }

    public static void setPKIfNull(Object entity, Object pkValue) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException {
        Field pkField = getPK(entity.getClass());
        if (pkField == null) {
            throw new RuntimeException("实体:" + entity + "找不到主键字段");
        } else {
            pkField.setAccessible(true);
            if (pkField.get(entity) == null) {
                pkField.set(entity, getValue(pkField, pkValue));
            }

        }
    }

    public static Object getPK(Object entity) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
        Field pkField = getPK(entity.getClass());
        if (pkField == null) {
            throw new RuntimeException("实体:" + entity + "找不到主键字段");
        } else {
            pkField.setAccessible(true);
            return pkField.get(entity);
        }
    }

    public static Field getPK(Class<?> entityClass) throws ClassNotFoundException {
        Field[] fields = entityClass.getDeclaredFields();
        int length = fields.length;

        for(int i = 0; i < length; ++i) {
            Field field = fields[i];
            if (field.getAnnotation(Column.class) != null && ((Column)field.getAnnotation(Column.class)).type() != ColumnType.normal) {
                return field;
            }
        }

        if (!entityClass.getSuperclass().getSimpleName().equals("Object")) {
            return getPK(entityClass.getSuperclass());
        } else {
            return null;
        }
    }

    public static Field getFieldByName(Class<?> entityClass, String fieldName) {
        Field[] fields = entityClass.getDeclaredFields();
        int length = fields.length;

        for(int i = 0; i < length; ++i) {
            Field field = fields[i];
            if (field.getName().equals(fieldName) && (field.getAnnotation(Column.class) != null || field.getAnnotation(Children.class) != null)) {
                return field;
            }
        }

        if (!entityClass.getSuperclass().getSimpleName().equals("Object")) {
            return getFieldByName(entityClass.getSuperclass(), fieldName);
        } else {
            return null;
        }
    }

    private static Object getValue(Field field, Object value) {
        String type = field.getGenericType().toString();
        if (field.getGenericType().equals(value.getClass())) {
            return value;
        } else if (type.equals("class java.lang.Long")) {
            return Long.parseLong((String)value);
        } else {
            return type.equals("class java.lang.Integer") ? Integer.parseInt((String)value) : value;
        }
    }
}
