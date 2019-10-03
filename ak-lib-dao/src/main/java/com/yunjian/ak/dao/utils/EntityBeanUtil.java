package com.yunjian.ak.dao.utils;

import com.yunjian.ak.dao.annotation.Children;
import com.yunjian.ak.dao.annotation.Column;
import com.yunjian.ak.dao.annotation.Entity;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 12:20
 * @Version 1.0
 */
public class EntityBeanUtil {
    public EntityBeanUtil() {
    }

    public static Class<?> getClass(String entity) throws ClassNotFoundException {
        return Class.forName(getClazzName(entity));
    }

    public static String getClazzName(String entity) {
        String type = entity;
        if (entity.startsWith("entity:")) {
            type = entity.substring("entity:".length());
        }

        int index = type.lastIndexOf(".");
        String packageName = type.substring(0, index + 1);
        String className = type.substring(index + 1);
        return packageName + firstCharUpperCase(className);
    }

    public static final String firstCharUpperCase(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static Object toBean(Class<?> clazz, Map<String, Object> entityData) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Object instance = clazz.newInstance();
        BeanUtils.populate(instance, entityData);
        return instance;
    }

    public static Map<String, Object> toMap(Object entity) throws IllegalAccessException, InvocationTargetException {
        Map<String, Object> map = new HashMap();
        Iterator iterator = getFields(entity.getClass(), new ArrayList()).iterator();

        while(iterator.hasNext()) {
            Field field = (Field)iterator.next();
            if (isEntityField(field)) {
                field.setAccessible(true);
                Object value = field.get(entity);
                if (value != null) {
                    if (isEntityObj(value)) {
                        map.put(field.getName(), toMap(value));
                    } else {
                        map.put(field.getName(), value);
                    }
                }
            }
        }

        return map;
    }

    public static boolean isEntityField(Field field) {
        return field.getAnnotation(Column.class) != null || field.getAnnotation(Children.class) != null;
    }

    public static Object toBean(String entity, Map<String, Object> entityData) throws InstantiationException, IllegalAccessException, ClassNotFoundException, InvocationTargetException {
        Object instance = getClass(entity).newInstance();
        BeanUtils.populate(instance, entityData);
        return instance;
    }

    public static Class<?> getGenericClazByField(Field field) {
        Class<?> clzz = null;
        if (field.getGenericType() instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType)field.getGenericType();
            clzz = (Class)pType.getActualTypeArguments()[0];
        }

        return clzz;
    }

    public static boolean isEntityObj(Object obj) {
        return obj.getClass().getAnnotation(Entity.class) != null;
    }

    public static boolean isEntityClazz(Class<?> type) {
        return type.getAnnotation(Entity.class) != null;
    }

    public static boolean isPrimitive(Object obj) {
        return obj instanceof String || obj instanceof Number || obj instanceof Boolean || obj instanceof Character;
    }

    public static List<Field> getFields(Class<?> subEntityClazz, List<Field> fieldList) {
        if (!subEntityClazz.getSuperclass().getSimpleName().equals("Object")) {
            getFields(subEntityClazz.getSuperclass(), fieldList);
        }

        Collections.addAll(fieldList, subEntityClazz.getDeclaredFields());
        return fieldList;
    }
}
