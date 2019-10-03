package com.yunjian.ak.dao.mybatis.interceptor;

import com.google.common.collect.Lists;
import com.yunjian.ak.dao.mybatis.dialect.*;
import com.yunjian.ak.dao.mybatis.enhance.*;
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
 * @Description: 系统分页插件
 * @Author: yong.sun
 * @Date: 2019/5/24 10:19
 * @Version 1.0
 */
@Intercepts({@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
)})
public class SYSPageInterceptor implements Interceptor {
    private List<Dialect> dialects;

    public SYSPageInterceptor() {
    }

    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        Object parameter = args[1];
        if (parameter != null && !(args[2] instanceof AkRowBounds)) {
            MappedStatement ms = (MappedStatement)args[0];
            BoundSql boundSql = ms.getBoundSql(parameter);

            // 添加数据权限过滤
            SQLPermissionBuilder.build(ms, boundSql);

            DaoParamInfo daoParamInfo = MybatisExtractor.extractParam(parameter);
            String sql = boundSql.getSql();
            String parsedSql = sql;
            if (daoParamInfo.getDynamicQueryParam() != null) {
                parsedSql = DynamicQuerySQLParser.parse(sql, ms);
            }

            Sortable sortable = daoParamInfo.getSortable();
            if (sortable != null && sortable.getOrders().size() > 0) {
                parsedSql = OrderSQLBuilder.build(parsedSql, ms, sortable);
            }

            Executor executor = (Executor)invocation.getTarget();
            Pageable pageable = (Pageable) daoParamInfo.getPageable();
            if (pageable != null) {
                Dialect dialect = MybatisExtractor.extractDialect(executor.getTransaction().getConnection(), this.dialects);
                if (dialect != null) {
                    return Lists.newArrayList(new Object[]{PageBuilder.build(executor, parameter, ms, (ResultHandler)args[3], boundSql, pageable, dialect, parsedSql, sql)});
                }
            }

            if (!sql.equals(parsedSql)) {
                MetaObject.forObject(boundSql, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory()).setValue("sql", parsedSql);
            }

            RowBounds rowBounds = (RowBounds)args[2];
            CacheKey key = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
            return executor.query(ms, parameter, rowBounds, (ResultHandler)args[3], key, boundSql);
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
