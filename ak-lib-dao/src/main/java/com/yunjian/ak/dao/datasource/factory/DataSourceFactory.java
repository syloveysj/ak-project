package com.yunjian.ak.dao.datasource.factory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @Description: 数据源工厂接口
 * @Author: yong.sun
 * @Date: 2019/5/22 9:34
 * @Version 1.0
 */
public interface DataSourceFactory {
    DataSource createDataSource(Properties properties) throws Exception;
}
