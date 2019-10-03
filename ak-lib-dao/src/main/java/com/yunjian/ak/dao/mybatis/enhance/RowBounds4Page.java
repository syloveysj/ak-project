package com.yunjian.ak.dao.mybatis.enhance;

import org.apache.ibatis.session.RowBounds;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 10:32
 * @Version 1.0
 */
public class RowBounds4Page extends RowBounds implements AkRowBounds {
    private Sortable sort;
    private Conditions condition;
    private boolean isCount;
    private int totalSize;

    public RowBounds4Page(int offset, int limit) {
        this(offset, limit, (Sortable)null);
    }

    public RowBounds4Page(int offset, int limit, Sortable sort) {
        this(offset, limit, sort, (Conditions)null);
    }

    public RowBounds4Page(int offset, int limit, Sortable sort, Conditions condition) {
        super(offset, limit);
        this.sort = sort;
        this.condition = condition;
    }

    public RowBounds4Page(Pageable pageable, Sortable sort, Conditions condition, boolean isCount) {
        super(((pageable == null ? 1 : pageable.getPageIndex()) - 1) * (pageable == null ? 15 : pageable.getPageSize()), pageable == null ? 15 : pageable.getPageSize());
        this.condition = condition == null ? null : condition;
        this.sort = sort == null ? null : sort;
        this.isCount = isCount;
    }

    public RowBounds4Page(int offset, int limit, Sortable sort, Conditions condition, boolean isCount) {
        this(offset, limit, sort, condition);
        this.isCount = isCount;
    }

    public Sortable getSort() {
        return this.sort;
    }

    public void setSort(Sortable sort) {
        this.sort = sort;
    }

    public Conditions getCondition() {
        return this.condition;
    }

    public void setCondition(Conditions condition) {
        this.condition = condition;
    }

    public boolean isCount() {
        return this.isCount;
    }

    public void setCount(boolean isCount) {
        this.isCount = isCount;
    }

    public int getTotalSize() {
        return this.totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public boolean isPaging() {
        return true;
    }
}
