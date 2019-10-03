package com.yunjian.ak.dao.core.impl;

import com.yunjian.ak.dao.core.Dao;
import com.yunjian.ak.dao.core.DaoFactory;
import com.yunjian.ak.dao.core.PKWrapper;
import com.yunjian.ak.dao.utils.PrimaryKeyUtil;
import com.yunjian.ak.utils.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 12:16
 * @Version 1.0
 */
public class PKWrapperWithUUID implements PKWrapper {
    public static PKWrapperWithUUID INSTANCE = new PKWrapperWithUUID();
    private static final Logger LOGGER = LoggerFactory.getLogger(PKWrapperWithUUID.class);

    private PKWrapperWithUUID() {
    }

    public Object wrapSingleEntityWithPK(Object entity) {
        try {
            Dao dao = DaoFactory.create(entity.getClass());
            PrimaryKeyUtil.setPKWithFieldName(entity, PrimaryKeyUtil.getPK(entity.getClass()).getName(), UUIDUtil.createUUID());

            while(dao.selectByID((String)PrimaryKeyUtil.getPK(entity)) != null) {
                PrimaryKeyUtil.setPKWithFieldName(entity, PrimaryKeyUtil.getPK(entity.getClass()).getName(), UUIDUtil.createUUID());
            }

            return entity;
        } catch (Exception e) {
            LOGGER.warn("主键包装时出现异常...异常信息为:" + (e.getCause() == null ? e.getMessage() : e.getCause().getMessage()));
            return entity;
        }
    }

    public List<Object> wrapListEntityWithPK(List<Object> list) {
        try {
            String pkStr = PrimaryKeyUtil.getPK(list.get(0).getClass()).getName();
            Iterator iterator = list.iterator();

            while(iterator.hasNext()) {
                Object entity = iterator.next();
                if (PrimaryKeyUtil.getPK(entity) == null) {
                    PrimaryKeyUtil.setPKWithFieldName(entity, pkStr, UUIDUtil.createUUID());
                }
            }

            return list;
        } catch (Exception e) {
            LOGGER.warn("主键包装时出现异常!{}", e.getMessage());
            return list;
        }
    }
}
