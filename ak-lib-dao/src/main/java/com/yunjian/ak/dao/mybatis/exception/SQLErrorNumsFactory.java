package com.yunjian.ak.dao.mybatis.exception;

import com.yunjian.ak.ioc.ApplicationContextManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;

import java.util.Collections;
import java.util.Map;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 13:44
 * @Version 1.0
 */
public class SQLErrorNumsFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(SQLErrorNumsFactory.class);
    private static final SQLErrorNumsFactory instance = new SQLErrorNumsFactory();
    private final Map<String, SQLErrorNums> errorNumsMap;

    public static SQLErrorNumsFactory getInstance() {
        return instance;
    }

    public SQLErrorNumsFactory() {
        Map errorNums;
        try {
            errorNums = ApplicationContextManager.getContext().getBeansOfType(SQLErrorNums.class, true, false);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("SQLErrorNums loaded: " + errorNums.keySet());
            }
        } catch (BeansException e) {
            LOGGER.warn("读取SQL错误编码配置文件失败...", e);
            errorNums = Collections.emptyMap();
        }

        this.errorNumsMap = errorNums;
    }

    public SQLErrorNums getErrorNums(String dbName) {
        SQLErrorNums sen = (SQLErrorNums)this.errorNumsMap.get(dbName + "_Error_Nums");
        return sen != null ? sen : null;
    }
}
