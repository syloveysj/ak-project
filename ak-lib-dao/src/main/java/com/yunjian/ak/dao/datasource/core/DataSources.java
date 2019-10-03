package com.yunjian.ak.dao.datasource.core;

import java.util.List;

/**
 * @Description: 数据源集合接口
 * @Author: yong.sun
 * @Date: 2019/5/22 9:34
 * @Version 1.0
 */
public interface DataSources {
    String getId();

    String getShortId();

    String getModelType();

    String getName();

    void setName(String name);

    String getDesc();

    List<DataSource> getDatasource();

    String getDefault();

    void setDefault(String def);

    void addDatasource(DataSource dataSource);
}