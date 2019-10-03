package com.yunjian.ak.dao.datasource.core;

import java.util.List;

/**
 * @Description: 数据源接口
 * @Author: yong.sun
 * @Date: 2019/5/22 9:34
 * @Version 1.0
 */
public interface DataSource {
    String getDriverClassName();

    void setDriverClassName(String driverClassName);

    String getUrl();

    void setUrl(String url);

    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String password);

    boolean isDefault();

    void setDefault(boolean b);

    void unsetDefault();

    boolean isSetDefault();

    String getId();

    void setId(String id);

    String getName();

    void setName(String name);

    boolean isSys();

    void setSys(boolean sys);

    void unsetSys();

    boolean isSetSys();

    DBType getType();

    void setType(DBType dbType);

    void unsetType();

    boolean isSetType();

    boolean isIsEncrypt();

    void setIsEncrypt(boolean isEncrypt);

    void unsetIsEncrypt();

    boolean isSetIsEncrypt();

    PoolType getPoolType();

    void setPoolType(PoolType poolType);

    String getDataSourceFactory();

    void setDataSourceFactory(String dataSourceFactory);

    void unsetDataSourceFactory();

    boolean isSetDataSourceFactory();

    List<PoolAttribute> getPoolAttribute();

    void addPoolAttribute(PoolAttribute poolAttribute);

    void setIsTenant(boolean isTenant);

    boolean isTenant();
}
