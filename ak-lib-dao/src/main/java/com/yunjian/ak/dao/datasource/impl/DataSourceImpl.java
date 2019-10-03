package com.yunjian.ak.dao.datasource.impl;

import com.yunjian.ak.dao.datasource.core.DBType;
import com.yunjian.ak.dao.datasource.core.DataSource;
import com.yunjian.ak.dao.datasource.core.PoolAttribute;
import com.yunjian.ak.dao.datasource.core.PoolType;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/22 13:52
 * @Version 1.0
 */
public class DataSourceImpl implements DataSource {
    protected static final String DRIVER_CLASS_NAME_EDEFAULT = null;
    protected String driverClassName;
    protected static final String URL_EDEFAULT = null;
    protected String url;
    protected static final String USERNAME_EDEFAULT = null;
    protected String username;
    protected static final String PASSWORD_EDEFAULT = null;
    protected String password;
    protected static final boolean DEFAULT_EDEFAULT = false;
    protected boolean default_;
    protected boolean defaultESet;
    protected static final String ID_EDEFAULT = null;
    protected String id;
    protected static final String NAME_EDEFAULT = null;
    protected String name;
    protected static final boolean SYS_EDEFAULT = false;
    protected boolean sys;
    protected boolean sysESet;
    protected static final DBType TYPE_EDEFAULT;
    protected DBType type;
    protected boolean typeESet;
    protected static final boolean IS_ENCRYPT_EDEFAULT = false;
    protected boolean isEncrypt;
    protected boolean isEncryptESet;
    protected static final PoolType POOL_TYPE_EDEFAULT;
    protected PoolType poolType;
    protected static final String DATA_SOURCE_FACTORY_EDEFAULT;
    protected String dataSourceFactory;
    protected boolean dataSourceFactoryESet;
    protected List<PoolAttribute> poolAttribute;
    protected boolean isTenant;
    protected static final boolean IS_TENANT_EDEFAULT = false;

    static {
        TYPE_EDEFAULT = DBType.MY_SQL;
        POOL_TYPE_EDEFAULT = PoolType.DBCP;
        DATA_SOURCE_FACTORY_EDEFAULT = null;
    }

    protected DataSourceImpl() {
        this.driverClassName = DRIVER_CLASS_NAME_EDEFAULT;
        this.url = URL_EDEFAULT;
        this.username = USERNAME_EDEFAULT;
        this.password = PASSWORD_EDEFAULT;
        this.default_ = DEFAULT_EDEFAULT;
        this.id = ID_EDEFAULT;
        this.name = NAME_EDEFAULT;
        this.sys = SYS_EDEFAULT;
        this.type = TYPE_EDEFAULT;
        this.isEncrypt = IS_ENCRYPT_EDEFAULT;
        this.poolType = POOL_TYPE_EDEFAULT;
        this.dataSourceFactory = DATA_SOURCE_FACTORY_EDEFAULT;
        this.isTenant = IS_TENANT_EDEFAULT;
    }

    public String getDriverClassName() {
        return this.driverClassName;
    }

    public void setDriverClassName(String newDriverClassName) {
        this.driverClassName = newDriverClassName;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String newUrl) {
        this.url = newUrl;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String newUsername) {
        this.username = newUsername;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public boolean isDefault() {
        return this.default_;
    }

    public void setDefault(boolean newDefault) {
        this.default_ = newDefault;
        this.defaultESet = true;
    }

    public void unsetDefault() {
        this.default_ = false;
        this.defaultESet = false;
    }

    public boolean isSetDefault() {
        return this.defaultESet;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String newId) {
        this.id = newId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public boolean isSys() {
        return this.sys;
    }

    public void setSys(boolean newSys) {
        this.sys = newSys;
        this.sysESet = true;
    }

    public void unsetSys() {
        this.sys = false;
        this.sysESet = false;
    }

    public boolean isSetSys() {
        return this.sysESet;
    }

    public DBType getType() {
        return this.type;
    }

    public void setType(DBType newType) {
        this.type = newType == null ? TYPE_EDEFAULT : newType;
        this.typeESet = true;
    }

    public void unsetType() {
        boolean oldTypeESet = this.typeESet;
        this.typeESet = false;
    }

    public boolean isSetType() {
        return this.typeESet;
    }

    public boolean isIsEncrypt() {
        return this.isEncrypt;
    }

    public void setIsEncrypt(boolean newIsEncrypt) {
        this.isEncrypt = newIsEncrypt;
        this.isEncryptESet = true;
    }

    public void unsetIsEncrypt() {
        this.isEncrypt = false;
        this.isEncryptESet = false;
    }

    public boolean isSetIsEncrypt() {
        return this.isEncryptESet;
    }

    public PoolType getPoolType() {
        return this.poolType;
    }

    public void setPoolType(PoolType newPoolType) {
        this.poolType = newPoolType == null ? POOL_TYPE_EDEFAULT : newPoolType;
    }

    public String getDataSourceFactory() {
        return this.dataSourceFactory;
    }

    public void setDataSourceFactory(String newDataSourceFactory) {
        this.dataSourceFactory = newDataSourceFactory;
        this.dataSourceFactoryESet = true;
    }

    public void unsetDataSourceFactory() {
        this.dataSourceFactory = DATA_SOURCE_FACTORY_EDEFAULT;
        this.dataSourceFactoryESet = false;
    }

    public boolean isSetDataSourceFactory() {
        return this.dataSourceFactoryESet;
    }

    public List<PoolAttribute> getPoolAttribute() {
        if (this.poolAttribute == null) {
            this.poolAttribute = new ArrayList<PoolAttribute>();
        }

        return this.poolAttribute;
    }

    public void addPoolAttribute(PoolAttribute poolAttribute) {
        if (this.poolAttribute == null) {
            this.poolAttribute = new ArrayList<PoolAttribute>();
        }

        this.poolAttribute.add(poolAttribute);
    }

    public void setIsTenant(boolean isTenant) {
        this.isTenant = isTenant;
    }

    public boolean isTenant() {
        return this.isTenant;
    }

    public String toString() {
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (driverClassName: ");
        result.append(this.driverClassName);
        result.append(", url: ");
        result.append(this.url);
        result.append(", username: ");
        result.append(this.username);
        result.append(", password: ");
        result.append(this.password);
        result.append(", default: ");
        if (this.defaultESet) {
            result.append(this.default_);
        } else {
            result.append("<unset>");
        }

        result.append(", id: ");
        result.append(this.id);
        result.append(", name: ");
        result.append(this.name);
        result.append(", sys: ");
        if (this.sysESet) {
            result.append(this.sys);
        } else {
            result.append("<unset>");
        }

        result.append(", type: ");
        if (this.typeESet) {
            result.append(this.type);
        } else {
            result.append("<unset>");
        }

        result.append(", isEncrypt: ");
        if (this.isEncryptESet) {
            result.append(this.isEncrypt);
        } else {
            result.append("<unset>");
        }

        result.append(", poolType: ");
        result.append(this.poolType);
        result.append(", dataSourceFactory: ");
        if (this.dataSourceFactoryESet) {
            result.append(this.dataSourceFactory);
        } else {
            result.append("<unset>");
        }

        result.append(')');
        return result.toString();
    }
}