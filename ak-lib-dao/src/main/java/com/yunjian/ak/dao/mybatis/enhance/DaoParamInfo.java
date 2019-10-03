package com.yunjian.ak.dao.mybatis.enhance;

import java.awt.print.Pageable;
import java.io.Serializable;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 10:13
 * @Version 1.0
 */
public class DaoParamInfo implements Serializable {
    private static final long serialVersionUID = 604868865642031016L;
    private Pageable pageable;
    private Sortable sortable;
    private DynamicQueryParam dynamicQueryParam;

    public DaoParamInfo() {
    }

    public Pageable getPageable() {
        return this.pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public Sortable getSortable() {
        return this.sortable;
    }

    public void setSortable(Sortable sortable) {
        this.sortable = sortable;
    }

    public DynamicQueryParam getDynamicQueryParam() {
        return this.dynamicQueryParam;
    }

    public void setDynamicQueryParam(DynamicQueryParam dynamicQueryParam) {
        this.dynamicQueryParam = dynamicQueryParam;
    }
}
