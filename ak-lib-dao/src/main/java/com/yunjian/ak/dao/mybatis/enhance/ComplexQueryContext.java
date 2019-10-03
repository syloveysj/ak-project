package com.yunjian.ak.dao.mybatis.enhance;

import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 9:47
 * @Version 1.0
 */
public class ComplexQueryContext extends RowBounds {
    private List<ResultMappingMeta> rmModels;

    public ComplexQueryContext(List<ResultMappingMeta> rmModels) {
        super(0, 2147483647);
        this.rmModels = rmModels;
    }

    public List<ResultMappingMeta> getRmModels() {
        return this.rmModels;
    }
}
