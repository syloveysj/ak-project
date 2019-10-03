package com.yunjian.ak.dao.mybatis.enhance;

import com.google.common.base.Preconditions;
import com.yunjian.ak.dao.mybatis.dialect.Dialect;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.print.Pageable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 10:13
 * @Version 1.0
 */
public class MybatisExtractor {
    private static final Logger LOGGER = LoggerFactory.getLogger(MybatisExtractor.class);
    private static final Map<String, Map<String, ResultMapping>> PROPERTY_RESULEMAPPING_CACHE = new ConcurrentHashMap();

    public MybatisExtractor() {
    }

    public static DaoParamInfo extractParam(Object parameterObject) {
        DaoParamInfo daoParamInfo = new DaoParamInfo();
        if (parameterObject != null && parameterObject instanceof Map) {
            Iterator iterator = ((Map)parameterObject).entrySet().iterator();

            while(true) {
                while(iterator.hasNext()) {
                    Map.Entry<String, Object> entry = (Map.Entry)iterator.next();
                    Object value = entry.getValue();
                    if (value != null && Pageable.class.isInstance(value)) {
                        daoParamInfo.setPageable((Pageable)value);
                    } else if (value != null && Sortable.class.isInstance(value)) {
                        daoParamInfo.setSortable((Sortable)value);
                    } else if (value != null && DynamicQueryParam.class.isInstance(value)) {
                        daoParamInfo.setDynamicQueryParam((DynamicQueryParam)value);
                    }
                }

                return daoParamInfo;
            }
        } else if (parameterObject != null && Pageable.class.isInstance(parameterObject)) {
            daoParamInfo.setPageable((Pageable)parameterObject);
        } else if (parameterObject != null && Sortable.class.isInstance(parameterObject)) {
            daoParamInfo.setSortable((Sortable)parameterObject);
        } else if (parameterObject != null && DynamicQueryParam.class.isInstance(parameterObject)) {
            daoParamInfo.setDynamicQueryParam((DynamicQueryParam)parameterObject);
        }

        return daoParamInfo;
    }

    public static String extractColumn(String property, MappedStatement mappedStatement) {
        Preconditions.checkArgument(mappedStatement != null, "参数mappedStatement不能为null");
        Iterator iterator = mappedStatement.getResultMaps().iterator();

        while(iterator.hasNext()) {
            ResultMap resultMap = (ResultMap)iterator.next();
            Iterator iterator1 = resultMap.getResultMappings().iterator();

            while(iterator1.hasNext()) {
                ResultMapping resultMapping = (ResultMapping)iterator1.next();
                if (resultMapping.getProperty().equals(property)) {
                    return resultMapping.getColumn();
                }
            }
        }

        return null;
    }

    public static Dialect extractDialect(Connection connection, List<Dialect> dialects) {
        if (connection != null && dialects != null && dialects.size() != 0) {
            try {
                DatabaseMetaData metaData = connection.getMetaData();
                String databaseType = metaData.getDatabaseProductName();
                int databaseVersion = metaData.getDatabaseMajorVersion();
                Iterator iterator = dialects.iterator();

                while(iterator.hasNext()) {
                    Dialect dialect = (Dialect)iterator.next();
                    if (dialect.isSupport(databaseType, databaseVersion)) {
                        return dialect;
                    }
                }

                LOGGER.warn("没有设置支持数据库类型：[{}]，数据库版本：[{}]的方言", databaseType, databaseVersion);
            } catch (SQLException e) {
                LOGGER.warn("获取数据库方言失败", e);
            }

            return null;
        } else {
            return null;
        }
    }

    public static ResultMapping extractColumnMapping(String property, MappedStatement mappedStatement) {
        Preconditions.checkArgument(mappedStatement != null, "参数mappedStatement不能为null");
        Iterator iterator = mappedStatement.getResultMaps().iterator();

        while(iterator.hasNext()) {
            ResultMap resultMap = (ResultMap)iterator.next();
            Iterator iterator1 = resultMap.getResultMappings().iterator();

            while(iterator1.hasNext()) {
                ResultMapping resultMapping = (ResultMapping)iterator1.next();
                if (resultMapping.getProperty().equals(property)) {
                    return resultMapping;
                }
            }
        }

        return null;
    }

    public static ResultMappingMeta extractColumnMapping(String property, MappedStatement mappedStatement, Configuration configuration) {
        Preconditions.checkArgument(mappedStatement != null, "参数mappedStatement不能为null");
        PropertyTokenizer prop = new PropertyTokenizer(property);
        String columnName = "";
        Iterator iterator;
        ResultMap resultMap;
        if (prop.hasNext()) {
            iterator = mappedStatement.getResultMaps().iterator();

            while(iterator.hasNext()) {
                resultMap = (ResultMap)iterator.next();
                Map<String, ResultMapping> rm = createResultMappingCacheIfNull(resultMap.getId(), configuration);
                if (rm == null) {
                    throw new RuntimeException("找不到属性:" + property + "对应的ResultMap");
                }

                if (rm.containsKey(prop.getIndexedName())) {
                    Preconditions.checkArgument(((ResultMapping)rm.get(prop.getIndexedName())).getNestedResultMapId() != null, "resultMap:" + resultMap.getId() + "中,属性" + property + "找不到子节点...");
                    columnName = columnName + (((ResultMapping)rm.get(prop.getIndexedName())).getColumnPrefix() == null ? "" : ((ResultMapping)rm.get(prop.getIndexedName())).getColumnPrefix());
                    return extractColumnMapping(prop.getChildren(), ((ResultMapping)rm.get(prop.getIndexedName())).getNestedResultMapId(), configuration, columnName);
                }
            }
        } else {
            iterator = mappedStatement.getResultMaps().iterator();

            while(iterator.hasNext()) {
                resultMap = (ResultMap)iterator.next();
                Iterator iterator1 = resultMap.getResultMappings().iterator();

                while(iterator1.hasNext()) {
                    ResultMapping resultMapping = (ResultMapping)iterator1.next();
                    if (resultMapping.getProperty().equals(property)) {
                        columnName = (resultMapping.getColumnPrefix() == null ? "" : resultMapping.getColumnPrefix()) + resultMapping.getColumn();
                        return new ResultMappingMeta(columnName, resultMapping);
                    }
                }
            }
        }

        return null;
    }

    private static ResultMappingMeta extractColumnMapping(String property, String resultMapID, Configuration configuration, String columnName) {
        Map<String, ResultMapping> rm = createResultMappingCacheIfNull(resultMapID, configuration);
        if (rm == null) {
            throw new RuntimeException("复杂查询解析过程中出现异常,找不到ResultMap:" + resultMapID);
        } else {
            PropertyTokenizer prop = new PropertyTokenizer(property);
            if (prop.hasNext()) {
                columnName = columnName + (((ResultMapping)rm.get(prop.getIndexedName())).getColumnPrefix() == null ? "" : ((ResultMapping)rm.get(prop.getIndexedName())).getColumnPrefix());
                return extractColumnMapping(prop.getChildren(), ((ResultMapping)rm.get(prop.getIndexedName())).getNestedResultMapId(), configuration, columnName);
            } else {
                columnName = columnName + (((ResultMapping)rm.get(prop.getIndexedName())).getColumn() == null ? "" : ((ResultMapping)rm.get(prop.getIndexedName())).getColumn());
                return new ResultMappingMeta(columnName, (ResultMapping)rm.get(prop.getIndexedName()));
            }
        }
    }

    private static Map<String, ResultMapping> createResultMappingCacheIfNull(String resultMapID, Configuration configuration) {
        if (!PROPERTY_RESULEMAPPING_CACHE.containsKey(resultMapID)) {
            ResultMap resultMap = configuration.getResultMap(resultMapID);
            Map<String, ResultMapping> map = new HashMap();
            Iterator iterator = resultMap.getPropertyResultMappings().iterator();

            while(iterator.hasNext()) {
                ResultMapping rm = (ResultMapping)iterator.next();
                map.put(rm.getProperty(), rm);
            }

            PROPERTY_RESULEMAPPING_CACHE.put(resultMapID, map);
        }

        return (Map)PROPERTY_RESULEMAPPING_CACHE.get(resultMapID);
    }
}
