package com.yunjian.ak.dao.mybatis.enhance;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.yunjian.ak.dao.mybatis.exception.FrameworkSQLException;
import com.yunjian.ak.dao.mybatis.exception.PageException;
import com.yunjian.ak.dao.mybatis.utils.JDBCUtils;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 10:24
 * @Version 1.0
 */
public class SQLResultSetCounter {
    private static final String RESULT_FIELD = "AK_RECORDS_TOTAL";
    private static final String COUNT_SQL_TEMPLATE = "SELECT count(1) AS %s FROM (%s) ak_records";
    private static final Logger LOGGER = LoggerFactory.getLogger(SQLResultSetCounter.class);

    public SQLResultSetCounter() {
    }

    public static int getValue(Connection connection, String sql, MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject, RowBounds defaultRowBounds) {
        Preconditions.checkArgument(connection != null, "参数connection不能为null");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(sql), "参数sql不能为null");
        Preconditions.checkArgument(mappedStatement != null, "参数mappedStatement不能为null");
        Preconditions.checkArgument(boundSql != null, "参数boundSql不能为null");
        String countableSQL = String.format("SELECT count(1) AS %s FROM (%s) ak_records", "AK_RECORDS_TOTAL", sql);
        LOGGER.debug("查询总数记录SQL：{}", countableSQL);
        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        int i;
        try {
            ConnectionTenantScheme.change(connection);

            preparedStatement = connection.prepareStatement(countableSQL);
            parameterHandler.setParameters(preparedStatement);
            if (defaultRowBounds instanceof ComplexQueryContext) {
                i = boundSql.getParameterMappings().size();
                TypeHandlerRegistry typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
                Iterator iterator = ((ComplexQueryContext)defaultRowBounds).getRmModels().iterator();

                while(iterator.hasNext()) {
                    ResultMappingMeta resultMapping = (ResultMappingMeta)iterator.next();
                    TypeHandler typeHandler = typeHandlerRegistry.getTypeHandler(resultMapping.getJavaType(), resultMapping.getJdbcType());
                    ++i;
                    typeHandler.setParameter(preparedStatement, i, resultMapping.getData(), resultMapping.getJdbcType());
                }
            }

            resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                throw new PageException("查询总数失败");
            }

            i = resultSet.getInt("AK_RECORDS_TOTAL");
        } catch (Exception e) {
            throw new FrameworkSQLException("查询记录总数失败", e);
        } finally {
            JDBCUtils.closeResultset(resultSet);
            JDBCUtils.closeStatement(preparedStatement);
        }

        return i;
    }
}
