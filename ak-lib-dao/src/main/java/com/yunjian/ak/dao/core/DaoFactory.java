package com.yunjian.ak.dao.core;

import com.yunjian.ak.dao.annotation.Entity;
import com.yunjian.ak.dao.utils.EntityBeanUtil;
import com.yunjian.ak.ioc.ApplicationContextManager;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 12:17
 * @Version 1.0
 */
public class DaoFactory {
    public DaoFactory() {
    }

    public static <T> Dao<T> create(Class<T> entityClass) {
        Dao<T> dao = (Dao) ApplicationContextManager.getContext().getBean("baseDao");
        Entity entity = null;
        if ((entity = (Entity)entityClass.getAnnotation(Entity.class)) != null) {
            dao.setNamespace(EntityBeanUtil.getClazzName(entity.id()) + "Mapper");
            dao.setDataSource(entity.ds());
            dao.setLogicDelete(entity.logicDelete());
        }

        return dao;
    }

    public static <T> Dao<T> create(Class<T> entityClass, String datasourceid) {
        Dao<T> dao = (Dao)ApplicationContextManager.getContext().getBean("baseDao");
        dao.setDataSource(datasourceid);
        Entity entity = null;
        if ((entity = (Entity)entityClass.getAnnotation(Entity.class)) != null) {
            dao.setNamespace(EntityBeanUtil.getClazzName(entity.id()) + "Mapper");
            dao.setLogicDelete(entity.logicDelete());
        }

        return dao;
    }
}
