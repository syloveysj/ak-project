package com.yunjian.ak.dao.mybatis.enhance;

import com.google.common.base.Preconditions;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 10:23
 * @Version 1.0
 */
public class Page<T> implements Serializable {
    private static final long serialVersionUID = 3849600749834631930L;
    private int pageIndex;
    private int pageSize;
    private int total;
    private List<T> contents;

    public Page() {
    }

    public Page(Pageable pageable, List<T> contents) {
        Preconditions.checkArgument(pageable != null, "参数pageable不能为null");
        this.pageIndex = pageable.getPageIndex();
        this.pageSize = pageable.getPageSize();
        this.total = pageable.getTotal();
        this.contents = contents;
    }

    public Page(int pageIndex, int pageSize, int total, List<T> contents) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.total = total;
        this.contents = contents;
    }

    public int getPageIndex() {
        return this.pageIndex;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getContents() {
        return this.contents;
    }

    public int getTotalPage() {
        return (this.total + this.pageSize - 1) / this.pageSize;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setContents(List<T> contents) {
        this.contents = contents;
    }
}

