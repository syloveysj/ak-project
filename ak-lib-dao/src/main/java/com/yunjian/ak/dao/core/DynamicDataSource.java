package com.yunjian.ak.dao.core;

import com.yunjian.ak.config.ConfigManager;
import com.yunjian.ak.dao.annotation.Entity;
import com.yunjian.ak.dao.datasource.core.*;
import com.yunjian.ak.dao.datasource.impl.DataSourcesConverter;
import com.yunjian.ak.dao.datasource.impl.EmptyDataSource;
import com.yunjian.ak.health.AkHealthCheck;
import com.yunjian.ak.health.HealthCheckResult;
import com.yunjian.ak.health.HealthStatus;
import com.yunjian.ak.res.ResourceManager;
import com.yunjian.ak.utils.DESEncrypt;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DynamicDataSource extends AbstractRoutingDataSource implements AkHealthCheck {
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSource.class);
    protected static final ThreadLocal<String> contextHolder = new ThreadLocal();
    protected static Map<String, Properties> databaseProperties = new HashMap();
    protected static List<String> sysDSList = new ArrayList();
    protected static String defaultDSName;
    protected Map<Object, Object> targetDS;
    private boolean isDatasourceConfigContainPlaceHolder;
    private static Boolean reloadDatasourceEnable = null;
    private static boolean isDatasourceInited;

    public DynamicDataSource() {
    }

    /**
     * 数据库连接工厂初始化
     * @return
     */
    protected Map<Object, Object> init() {
        LOGGER.debug("数据库连接工厂初始化开始...");
        InputStream inputStream = ResourceManager.getInstance().getDefineContent("datasource.xml");
        if (inputStream != null) {
            Map<Object, Object> dataSources = new LinkedHashMap();
            dataSources.putAll(this.getDataSources(inputStream));
            LOGGER.debug("数据库连接工厂初始化结束");
            return dataSources;
        } else {
            LOGGER.error("没有找到数据源配置文件，持久层功能将不可用");
            Map<Object, Object> dataSources = new HashMap(1);
            dataSources.put("$emptyDS", new EmptyDataSource());
            return dataSources;
        }
    }

    public void afterPropertiesSet() {
        Map<Object, Object> dataSources = this.init();
        if (!dataSources.isEmpty()) {
            if (StringUtils.isNotEmpty(defaultDSName)) {
                this.setDefaultTargetDataSource(dataSources.get(defaultDSName));
            } else {
                this.setDefaultTargetDataSource(dataSources.values().iterator().next());
            }

            this.targetDS = dataSources;
            this.setTargetDataSources(dataSources);
            super.afterPropertiesSet();
        } else {
            LOGGER.warn("没有配置任何数据源，持久层相关功能将全部不可用!");
        }

        isDatasourceInited = true;
    }

    /**
     * 获取所有数据源
     * @param inputStream
     * @return
     */
    protected Map<String, DataSource> getDataSources(InputStream inputStream) {
        Map<String, DataSource> dataSources = new LinkedHashMap();
        databaseProperties = this.getDataSourceConfigs(inputStream);
        Iterator iterator = databaseProperties.entrySet().iterator();

        while(iterator.hasNext()) {
            Map.Entry entry = (Map.Entry)iterator.next();

            try {
                Properties properties = (Properties)entry.getValue();
                DataSource dataSource = null;
                if (properties.containsKey("sys") && Boolean.parseBoolean(properties.get("sys").toString())) {
                    sysDSList.add(entry.getKey().toString());
                }

                dataSource = this.generateDataSource(this.resolvePlaceHolder(properties), (String)entry.getKey());
                if (dataSource != null) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("数据源[" + (String)entry.getKey() + "]初始化完成，配置：" + ((Properties)entry.getValue()).toString());
                    }

                    Boolean isDefault = false;
                    if (properties.containsKey("default")) {
                        isDefault = Boolean.parseBoolean(((Properties)entry.getValue()).get("default").toString());
                    }

                    if (isDefault) {
                        defaultDSName = (String)entry.getKey();
                    }

                    dataSources.put(entry.getKey().toString(), dataSource);
                }
            } catch (Exception e) {
                LOGGER.error("创建数据源" + (String)entry.getKey() + "的过程中发送错误，创建失败，请检查数据源配置文件", e);
            }
        }

        return dataSources;
    }

    /**
     * 配置文件中占位符处理
     * @param properties
     * @return
     */
    private Properties resolvePlaceHolder(Properties properties) {
        ConfigManager config = ConfigManager.getInstance();
        Iterator<Map.Entry<Object, Object>> it = properties.entrySet().iterator();
        boolean containPlaceHolder = false;

        while(it.hasNext()) {
            Map.Entry<Object, Object> entry = (Map.Entry)it.next();
            StringBuffer sb = new StringBuffer();
            Pattern pattern = Pattern.compile("\\$\\{([^\\$\\{]*)\\}");
            Matcher matcher = pattern.matcher((String)entry.getValue());

            while(matcher.find()) {
                containPlaceHolder = true;
                String key = matcher.group(1);
                matcher.appendReplacement(sb, config.getConfig(key));
            }

            matcher.appendTail(sb);
            properties.put(entry.getKey(), sb.toString());
        }

        this.isDatasourceConfigContainPlaceHolder = containPlaceHolder;
        return properties;
    }

    /**
     * 生成数据源
     * @param properties
     * @param dsKey
     * @return
     * @throws Exception
     */
    protected DataSource generateDataSource(Properties properties, String dsKey) throws Exception {
        Object obj = Class.forName(properties.getProperty("dataSourceFactory")).newInstance();
        if (obj instanceof JNDIDataSource) {
            properties.setProperty("JNDIName", properties.getProperty("url"));
        }

        return (DataSource)obj.getClass().getMethod("createDataSource", Properties.class).invoke(obj, properties);
    }

    /**
     * 获取所有数据源配置
     * @param configFile
     * @return
     */
    protected Map<String, Properties> getDataSourceConfigs(InputStream configFile) {
        Map<String, Properties> result = new HashMap();
        DataSources object = DataSourcesConverter.read(configFile);
        com.yunjian.ak.dao.datasource.core.DataSource dataSource = null;
        Properties properties;
        if (object != null) {
            for(Iterator<com.yunjian.ak.dao.datasource.core.DataSource> iterator = object.getDatasource().iterator(); iterator.hasNext(); result.put(dataSource.getId(), properties)) {
                dataSource = iterator.next();
                properties = new Properties();

                if(true) {
                    if(dataSource.isIsEncrypt()) {
                        DESEncrypt en = new DESEncrypt();
                        en.setKey("ak_key_password");
                        en.setDesString(dataSource.getPassword());
                        properties.setProperty("password", en.getStrM());
                    } else {
                        properties.setProperty("password", dataSource.getPassword());
                    }

                    properties.setProperty("driverClassName", dataSource.getDriverClassName());
                    properties.setProperty("url", dataSource.getUrl());
                    properties.setProperty("username", dataSource.getUsername());
                    properties.setProperty("dataSourceFactory", dataSource.getDataSourceFactory());

                    properties.setProperty("sys", String.valueOf(dataSource.isSys()));
                    properties.setProperty("default", String.valueOf(dataSource.isDefault()));
                    properties.setProperty("type", dataSource.getType().getName());
                    properties.setProperty("isEncrypt", String.valueOf(dataSource.isIsEncrypt()));
                    properties.setProperty("isTenant", String.valueOf(dataSource.isTenant()));
                    properties.setProperty("id", dataSource.getId());
                    properties.setProperty("name", dataSource.getName());

                    Iterator<PoolAttribute> poolAttribute = dataSource.getPoolAttribute().iterator();
                    while(poolAttribute.hasNext()) {
                        PoolAttribute attri = poolAttribute.next();
                        if (StringUtils.isNotBlank(attri.getValue())) {
                            properties.setProperty(attri.getKey(), attri.getValue());
                        }
                    }

                    properties.put("dataSourceFactory", StringUtils.isNotBlank(dataSource.getDataSourceFactory()) ? dataSource.getDataSourceFactory() : "com.yunjian.ak.dao.datasource.impl.DBCPDataSourceFactory");
                    if (dataSource instanceof DBCPDataSource) {
                        if (properties.getProperty("initialSize").equals("0")) {
                            properties.setProperty("initialSize", "5");
                        }

                        if (properties.getProperty("maxActive").equals("0")) {
                            properties.setProperty("maxActive", "20");
                        }

                        this.setPropertyIfNotConfig(properties, "timeBetweenEvictionRunsMillis", "5000");
                        this.setPropertyIfNotConfig(properties, "minEvictableIdleTimeMillis", "60000");
                        this.setPropertyIfNotConfig(properties, "testOnBorrow", "true");
                        this.setPropertyIfNotConfig(properties, "testWhileIdle", "true");
                        if (dataSource.getType().equals(DBType.ORACLE)) {
                            this.setPropertyIfNotConfig(properties, "validationQuery", "select 1 from dual");
                        } else if (DBType.DB2.equals(dataSource.getType())) {
                            this.setPropertyIfNotConfig(properties, "validationQuery", "select 1 from sysibm.sysdummy1");
                        } else {
                            this.setPropertyIfNotConfig(properties, "validationQuery", "select 1");
                        }
                    } else if (dataSource instanceof C3P0DataSource) {
                        if (properties.getProperty("maxPoolSize").equals("0")) {
                            properties.setProperty("maxPoolSize", "20");
                        }

                        if (properties.getProperty("minPoolSize").equals("0")) {
                            properties.setProperty("minPoolSize", "5");
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * 是否启用了重新加载数据源
     * @return
     */
    private boolean isReloadDatasourceEnabled() {
        if (reloadDatasourceEnable == null) {
            String reload = ConfigManager.getInstance().getConfig("ak.datasource.reload.enable");
            reloadDatasourceEnable = reload == null ? false : reload.trim().equalsIgnoreCase("true");
        }

        return reloadDatasourceEnable;
    }

    /**
     * 获取连接
     * @return
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        try {
            return super.getConnection();
        } catch (SQLException e) {
            if (this.isReloadDatasourceEnabled() && isDatasourceInited) {
                isDatasourceInited = !isDatasourceInited;
                if (this.isDatasourceConfigContainPlaceHolder) {
                    ConfigManager.reload();
                }

                this.afterPropertiesSet();
                return this.getConnection();
            } else {
                throw e;
            }
        }
    }

    /**
     * 设置属性文件缺省配置
     * @param properties
     * @param key
     * @param val
     */
    private void setPropertyIfNotConfig(Properties properties, String key, String val) {
        if (!properties.containsKey(key)) {
            properties.setProperty(key, val);
        }

    }

    /**
     * 确定当前的数据源
     * @return
     */
    protected Object determineCurrentLookupKey() {
        Object obj = getDataSourceKey();
        return obj;
    }

    /**
     * 确定目标数据源
     * @return
     */
    public DataSource determineTargetDataSource() {
        return super.determineTargetDataSource();
    }

    /**
     * 获取系统数据源Key
     * @return
     */
    public String getSysDataSourceKey() {
        return sysDSList.size() != 0 ? (String)sysDSList.get(0) : ("".equals(defaultDSName) ? getDataSourceKey() : defaultDSName);
    }

    /**
     * 获取系统数据源
     * @return
     */
    public synchronized DataSource getSysDataSource() {
        String curr = getDataSourceKey();
        setDataSourceKey(this.getSysDataSourceKey());
        DataSource sysDS = super.determineTargetDataSource();
        setDataSourceKey(curr);
        return sysDS;
    }

    /**
     * 设置数据源Key
     * @param customerType
     */
    public static void setDataSourceKey(String customerType) {
        contextHolder.set(customerType);
    }

    /**
     * 设置数据源Key
     * @param entityClazz
     */
    public static void setDataSourceKey(Class<?> entityClazz) {
        if (entityClazz.getAnnotation(Entity.class) != null) {
            contextHolder.set(((Entity)entityClazz.getAnnotation(Entity.class)).ds());
        }

    }

    public static String getDataSourceKey() {
        return (String)contextHolder.get();
    }

    public static void clearDataSourceKey() {
        contextHolder.remove();
    }

    public static String getDefaultDSName() {
        return defaultDSName;
    }

    public Map<Object, Object> getTargetDS() {
        return this.targetDS;
    }

    public static String getDSIDFromFeature(String feature) {
        return "sys".equals(feature) && sysDSList.size() != 0 ? (String)sysDSList.get(0) : feature;
    }

    public static Map<String, Properties> getDatabaseProperties() {
        return databaseProperties;
    }

    /**
     * 进行数据源的健康检测
     * @return
     */
    public HealthCheckResult check() {
        HealthCheckResult result = new HealthCheckResult("应用数据源连接池", HealthStatus.NORMAL);
        if (databaseProperties.isEmpty()) {
            return result;
        } else {
            Iterator iterator = databaseProperties.keySet().iterator();

            while(iterator.hasNext()) {
                String dsKey = (String)iterator.next();
                if (!this.targetDS.containsKey(dsKey)) {
                    result.setStatus(HealthStatus.ERROR);
                    result.setDesc("数据源ID:【" + dsKey + "】没有成功初始化.");
                }
            }

            return result;
        }
    }
}
