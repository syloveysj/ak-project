package com.yunjian.ak.dao.mybatis.enhance;

import org.apache.ibatis.session.RowBounds;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 13:52
 * @Version 1.0
 */
public class RowBounds4DynSQL extends RowBounds implements AkRowBounds {
    private Sortable sort;
    private Conditions condition;
    private boolean isCount;
    private int totalSize;

    public RowBounds4DynSQL(Sortable sort, Conditions condition, boolean isCount) {
        this.sort = sort;
        this.condition = condition;
        this.isCount = isCount;
    }

    public Conditions getCondition() {
        return this.condition;
    }

    public Sortable getSort() {
        return this.sort;
    }

    public boolean isCount() {
        return this.isCount;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public boolean isPaging() {
        return false;
    }

    public int getTotalSize() {
        return this.totalSize;
    }
}
