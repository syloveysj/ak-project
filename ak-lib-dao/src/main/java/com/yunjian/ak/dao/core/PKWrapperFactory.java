package com.yunjian.ak.dao.core;

import com.yunjian.ak.dao.annotation.Column;
import com.yunjian.ak.dao.core.impl.PKWrapperWithAutoIncreLong;
import com.yunjian.ak.dao.core.impl.PKWrapperWithUUID;
import com.yunjian.ak.dao.utils.PrimaryKeyUtil;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 11:44
 * @Version 1.0
 */
public class PKWrapperFactory {
    public PKWrapperFactory() {
    }

    public static PKWrapper getPKWrapper(Object entity) {
        Object wapper = null;

        try {
            String type = ((Column) PrimaryKeyUtil.getPK(entity.getClass()).getAnnotation(Column.class)).type().toString();
            if ("normal".equals(type.toLowerCase())) {
                return (PKWrapper)wapper;
            } else if ("incrementlong".equals(type.toLowerCase())) {
                return PKWrapperWithAutoIncreLong.INSTANCE;
            } else {
                return "uuid".equals(type.toLowerCase()) ? PKWrapperWithUUID.INSTANCE : null;
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("未找到主键包装器实体对象所对应的类", e);
        }
    }
}
