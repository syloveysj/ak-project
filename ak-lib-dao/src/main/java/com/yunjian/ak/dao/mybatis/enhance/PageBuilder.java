package com.yunjian.ak.dao.mybatis.enhance;

import com.google.common.base.Preconditions;
import com.yunjian.ak.dao.mybatis.dialect.Dialect;
import com.yunjian.ak.dao.mybatis.exception.PageException;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 10:24
 * @Version 1.0
 */
public class PageBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(PageBuilder.class);

    public PageBuilder() {
    }

    public static Page<Object> build(Executor executor, Object parameter, MappedStatement ms, ResultHandler resultHandler, BoundSql boundSql, Pageable pageable, Dialect dialect, String originalSql, String selectTotalSql) {
        Preconditions.checkArgument(executor != null, "参数executor不能为null");
        Preconditions.checkArgument(boundSql != null, "参数boundSql不能为null");
        Preconditions.checkArgument(pageable != null, "参数pageable不能为null");
        Preconditions.checkArgument(dialect != null, "参数dialect不能为null");
        Preconditions.checkArgument(originalSql != null, "参数originalSql不能为null");

        try {
            pageable.setTotal(SQLResultSetCounter.getValue(executor.getTransaction().getConnection(), dialect.getTotalSqlStr(selectTotalSql), ms, boundSql, parameter, (RowBounds)null));
            int offset = pageable.getOffset();
            int limit = pageable.getPageSize();
            String pageSql = dialect.getPagableSQL("", originalSql, offset, limit);
            LOGGER.debug("分页查询的SQL({}): {}", ms.getId(), pageSql);
            MetaObject.forObject(boundSql, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory()).setValue("sql", pageSql);
            CacheKey key = executor.createCacheKey(ms, parameter, new RowBounds(offset, limit), boundSql);
            List<Object> contents = executor.query(ms, parameter, new RowBounds(0, 2147483647), resultHandler, key, boundSql);
            return new Page(pageable, contents);
        } catch (SQLException e) {
            throw new PageException("分页失败", e);
        }
    }

    public static <T> List<T> build(Executor executor, Object parameter, MappedStatement ms, ResultHandler resultHandler, BoundSql boundSql, RowBounds4Page rowBounds, Dialect dialect, String parsedSql, String originalSql, RowBounds defaultRowBounds) {
        Preconditions.checkArgument(executor != null, "参数executor不能为null");
        Preconditions.checkArgument(boundSql != null, "参数boundSql不能为null");
        Preconditions.checkArgument(rowBounds != null, "参数pageable不能为null");
        Preconditions.checkArgument(dialect != null, "参数dialect不能为null");
        Preconditions.checkArgument(originalSql != null, "参数originalSql不能为null");

        try {
            int offset = rowBounds.getOffset();
            int limit = rowBounds.getLimit();
            String pageSql = dialect.getPagableSQL(originalSql, parsedSql, offset, limit);
            LOGGER.debug("分页查询的SQL({}): {}", ms.getId(), pageSql);
            MetaObject.forObject(boundSql, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory()).setValue("sql", pageSql);
            CacheKey key = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
            if (defaultRowBounds instanceof ComplexQueryContext) {
                WarpCacheKey(key, ((ComplexQueryContext)defaultRowBounds).getRmModels());
            }

            List<T> resultList = executor.query(ms, parameter, defaultRowBounds, resultHandler, key, boundSql);
            return resultList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new PageException("分页失败", e);
        }
    }

    public static void WarpCacheKey(CacheKey key, List<ResultMappingMeta> rms) {
        Iterator iterator = rms.iterator();

        while(iterator.hasNext()) {
            ResultMappingMeta rm = (ResultMappingMeta)iterator.next();
            key.update(rm.getData());
        }

    }
}
