package com.yunjian.ak.dao.mybatis.interceptor;

import com.yunjian.ak.dao.mybatis.dialect.*;
import com.yunjian.ak.dao.mybatis.enhance.*;
import com.yunjian.ak.dao.mybatis.utils.ConditionSqlBuilder;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * @Description: Mybatis分页插件
 * @Author: yong.sun
 * @Date: 2019/5/24 10:06
 * @Version 1.0
 */
@Intercepts({@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
)})
public class MybatisEnhanceInterceptor implements Interceptor {
    private List<Dialect> dialects;

    public MybatisEnhanceInterceptor() {
    }

    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        if (args[2] instanceof AkRowBounds) {
            Object parameter = args[1];
            MappedStatement ms = (MappedStatement)args[0];
            BoundSql boundSql = ms.getBoundSql(parameter);

            // 添加数据权限过滤
            SQLPermissionBuilder.build(ms, boundSql);

            String sql = boundSql.getSql();
            String parsedSql = sql;
            Executor executor = (Executor)invocation.getTarget();
            AkRowBounds rowBounds = (AkRowBounds)args[2];
            Dialect dialect = MybatisExtractor.extractDialect(executor.getTransaction().getConnection(), this.dialects);
            RowBounds defaultRowBounds = new RowBounds(0, 2147483647);
            if (rowBounds.getCondition() != null) {
                ConditionSqlBuilder sqlBuilder = (new ConditionSqlBuilder(sql, rowBounds.getCondition(), ms)).build();
                parsedSql = sqlBuilder.getSql();
                defaultRowBounds = new ComplexQueryContext(sqlBuilder.getSortedResultMappingModels());
            }

            if (rowBounds.getSort() != null && rowBounds.getSort().getOrders() != null && rowBounds.getSort().getOrders().size() > 0) {
                parsedSql = OrderSQLBuilder.build(parsedSql, ms, rowBounds.getSort());
            }

            if (rowBounds.isCount()) {
                rowBounds.setTotalSize(SQLResultSetCounter.getValue(executor.getTransaction().getConnection(), dialect.getTotalSqlStr(parsedSql), ms, boundSql, parameter, (RowBounds)defaultRowBounds));
            }

            if (rowBounds.isPaging() && dialect != null) {
                return PageBuilder.build(executor, parameter, ms, (ResultHandler)args[3], boundSql, (RowBounds4Page)rowBounds, dialect, parsedSql, sql, (RowBounds)defaultRowBounds);
            } else {
                if (!sql.equals(parsedSql)) {
                    MetaObject.forObject(boundSql, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory()).setValue("sql", parsedSql);
                }

                CacheKey key = executor.createCacheKey(ms, parameter, (RowBounds)rowBounds, boundSql);
                if (defaultRowBounds instanceof ComplexQueryContext) {
                    PageBuilder.WarpCacheKey(key, ((ComplexQueryContext)defaultRowBounds).getRmModels());
                }

                return executor.query(ms, parameter, (RowBounds)defaultRowBounds, (ResultHandler)args[3], key, boundSql);
            }
        } else {
            return invocation.proceed();
        }
    }

    public Object plugin(Object target) {
        return target instanceof Executor ? Plugin.wrap(target, this) : target;
    }

    public void setProperties(Properties properties) {
        if (!properties.containsKey("dialects") && this.dialects == null) {
            this.dialects = new LinkedList();
            this.dialects.add(new OracleDialect());
            this.dialects.add(new MysqlDialect());
            this.dialects.add(new SqlServer2012Dialect());
            this.dialects.add(new PostgreSqlDialect());
        }
    }

    public void setDialects(List<Dialect> dialects) {
        this.dialects = dialects;
    }
}