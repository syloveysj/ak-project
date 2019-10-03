package com.yunjian.ak.dao.mybatis.enhance;

/**
 * @Description: 逻辑分页接口
 * @Author: yong.sun
 * @Date: 2019/5/24 10:09
 * @Version 1.0
 */
public interface AkRowBounds {
    Conditions getCondition();

    Sortable getSort();

    boolean isCount();

    void setTotalSize(int totalSize);

    boolean isPaging();
}
