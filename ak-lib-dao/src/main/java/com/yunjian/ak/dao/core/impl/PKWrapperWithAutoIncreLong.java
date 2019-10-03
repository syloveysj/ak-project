package com.yunjian.ak.dao.core.impl;

import com.yunjian.ak.dao.core.PKWrapper;
import com.yunjian.ak.dao.utils.PrimaryKeyUtil;
import com.yunjian.ak.id.IDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 11:47
 * @Version 1.0
 */
public class PKWrapperWithAutoIncreLong implements PKWrapper {
    public static PKWrapperWithAutoIncreLong INSTANCE = new PKWrapperWithAutoIncreLong();
    private static final Logger LOGGER = LoggerFactory.getLogger(PKWrapperWithAutoIncreLong.class);

    private PKWrapperWithAutoIncreLong() {
    }

    public Object wrapSingleEntityWithPK(Object entity) {
        try {
            PrimaryKeyUtil.setPKWithFieldName(entity, PrimaryKeyUtil.getPK(entity.getClass()).getName(), IDGenerator.INSTANCE.createLong());
            return entity;
        } catch (Exception e) {
            LOGGER.warn("主键包装时出现异常!" + e.getMessage());
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
                    PrimaryKeyUtil.setPKWithFieldName(entity, pkStr, IDGenerator.INSTANCE.createLong());
                }
            }

            return list;
        } catch (Exception e) {
            LOGGER.warn("主键包装时出现异常!" + e.getMessage());
            return list;
        }
    }
}
