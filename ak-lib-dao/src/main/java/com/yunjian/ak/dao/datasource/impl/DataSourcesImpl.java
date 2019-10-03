package com.yunjian.ak.dao.datasource.impl;

import com.yunjian.ak.dao.datasource.core.DataSource;
import com.yunjian.ak.dao.datasource.core.DataSources;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/22 12:30
 * @Version 1.0
 */
public class DataSourcesImpl implements DataSources {
    protected List<DataSource> datasource;
    protected static final String DEFAULT_EDEFAULT = null;
    protected String default_;

    protected DataSourcesImpl() {
        this.default_ = DEFAULT_EDEFAULT;
    }

    public List<DataSource> getDatasource() {
        if (this.datasource == null) {
            this.datasource = new ArrayList<DataSource>();
        }

        return this.datasource;
    }

    public String getDefault() {
        return this.default_;
    }

    public void setDefault(String newDefault) {
        this.default_ = newDefault;
    }

    public void addDatasource(DataSource dataSource) {
        if (this.datasource == null) {
            this.datasource = new ArrayList<DataSource>();
        }
        this.datasource.add(dataSource);
    }

    public String toString() {
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (default: ");
        result.append(this.default_);
        result.append(')');
        return result.toString();
    }

    public String getId() {
        return "ds:ds";
    }

    public void setId(String id) {
    }

    public String getShortId() {
        return "ds";
    }

    public String getModelType() {
        return "xml";
    }

    public String getName() {
        return "datasource";
    }

    public void setName(String name) {
    }

    public String getDesc() {
        return "数据源配置";
    }
}
