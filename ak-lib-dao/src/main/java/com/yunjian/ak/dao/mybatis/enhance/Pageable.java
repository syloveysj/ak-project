package com.yunjian.ak.dao.mybatis.enhance;

import com.google.common.base.Preconditions;

import java.io.Serializable;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 10:23
 * @Version 1.0
 */
public class Pageable implements Serializable {
    private static final long serialVersionUID = 1773939835070923607L;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int PAGE_START_INDEX = 1;
    private int pageIndex;
    private int pageSize;
    private int total;

    public Pageable() {
        this(1, 10);
    }

    public Pageable(int pageIndex) {
        this(pageIndex, 10);
    }

    public Pageable(int pageIndex, int pageSize) {
        this.setPageIndex(pageIndex);
        this.setPageSize(pageSize);
    }

    public Pageable setPageIndex(int pageIndex) {
        Preconditions.checkArgument(pageIndex >= 1, "页面必须大于等于1");
        this.pageIndex = pageIndex;
        return this;
    }

    public int getPageIndex() {
        return this.pageIndex;
    }

    public Pageable setPageSize(int pageSize) {
        Preconditions.checkArgument(pageSize > 0, "页面大小必须大于0");
        this.pageSize = pageSize;
        return this;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    void setTotal(int total) {
        Preconditions.checkArgument(total >= 0, "总数不可能小于0");
        this.total = total;
        if (this.pageIndex * this.pageSize > total) {
            this.pageIndex = total / this.pageSize + (total % this.pageSize == 0 ? 0 : 1);
        }

    }

    int getTotal() {
        return this.total;
    }

    public int getOffset() {
        if (this.pageIndex < 1) {
            this.pageIndex = 1;
        }

        return (this.pageIndex - 1) * this.pageSize;
    }

    public String toString() {
        return "Pageable{pageIndex=" + this.pageIndex + ", pageSize=" + this.pageSize + "}";
    }
}
