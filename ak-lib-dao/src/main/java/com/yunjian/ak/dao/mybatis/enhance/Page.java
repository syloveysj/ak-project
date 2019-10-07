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
    private int page;
    private int pagesize;
    private int total;
    private List<T> rows;

    public Page() {
    }

    public Page(Pageable pageable, List<T> rows) {
        Preconditions.checkArgument(pageable != null, "参数pageable不能为null");
        this.page = pageable.getPageIndex();
        this.pagesize = pageable.getPageSize();
        this.total = pageable.getTotal();
        this.rows = rows;
    }

    public Page(int page, int pagesize, int total, List<T> rows) {
        this.page = page;
        this.pagesize = pagesize;
        this.total = total;
        this.rows = rows;
    }

    public int getPage() {
        return this.page;
    }

    public int getPagesize() {
        return this.pagesize;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getRows() {
        return this.rows;
    }

    public int getTotalPage() {
        return (this.total + this.pagesize - 1) / this.pagesize;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}

