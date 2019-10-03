package com.yunjian.ak.dao.mybatis.exception;

import java.util.Map;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 13:43
 * @Version 1.0
 */
public class SQLErrorNums {
    private Map<String, String> sqlErrorNums;

    public SQLErrorNums() {
    }

    public Map<String, String> getSqlErrorNums() {
        return this.sqlErrorNums;
    }

    public void setSqlErrorNums(Map<String, String> sqlErrorNums) {
        this.sqlErrorNums = sqlErrorNums;
    }
}
