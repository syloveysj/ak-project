package com.yunjian.ak.dao.mybatis;

import com.google.common.collect.Maps;
import com.yunjian.ak.dao.annotation.Entity;
import com.yunjian.ak.dao.core.Dao;
import com.yunjian.ak.dao.core.DynamicDataSource;
import com.yunjian.ak.dao.core.PKWrapper;
import com.yunjian.ak.dao.core.PKWrapperFactory;
import com.yunjian.ak.dao.mybatis.enhance.*;
import com.yunjian.ak.dao.mybatis.exception.AkDaoException;
import com.yunjian.ak.dao.mybatis.exception.AkDaoExceptionTranslator;
import com.yunjian.ak.dao.utils.EntityBeanUtil;
import com.yunjian.ak.dao.utils.PrimaryKeyUtil;
import com.yunjian.ak.ioc.ApplicationContextManager;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 11:40
 * @Version 1.0
 */
public class DefaultMybatisDao<T> extends SqlSessionDaoSupport implements Dao<T> {
    private String namespace;
    private String dataSourceID;
    private Boolean logicDelete;
    private static final String SQLID_INSERT = "insert";
    private static final String SQLID_INSERT_BATCH = "insertBatch";
    private static final String SQLID_UPDATE = "update";
    private static final String SQLID_UPDATE_NULL = "updateNull";
    private static final String SQLID_UPDATE_BATCH = "updateBatch";
    private static final String SQLID_DELETE = "delete";
    private static final String SQLID_DELETE_BATCH = "deleteBatch";
    private static final String SQLID_DELETE_LOGIC = "deleteLogic";
    private static final String SQLID_DELETE_BATCH_LOGIC = "deleteBatchLogic";
    private static final String SQLID_SELECT = "select";
    private static final String SQLID_SELECT_PAGE = "selectByPage";
    private static final String SQLID_SELECT_ALL = "selectAll";
    private static final String SQLID_SELECT_MANY = "selectMany";
    private static final String SQLID_SELECT_UNION = "selectUnion";
    private static final String SQLID_SELECT_BY_ID = "selectByID";
    private static final String SQLID_COUNT = "count";

    public DefaultMybatisDao() {
    }

    public String getNamespace() {
        return this.namespace;
    }

    private String getNamespace(Class<?> entityClass) {
        return StringUtils.isNotEmpty(this.namespace) ? this.namespace : entityClass.getName() + "Mapper";
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public int insert(T entity) {
        SqlSession sqlSession = null;

        try {
            PKWrapper wrapper;
            if (PrimaryKeyUtil.getPK(entity) == null && (wrapper = PKWrapperFactory.getPKWrapper(entity)) != null) {
                wrapper.wrapSingleEntityWithPK(entity);
            }

            sqlSession = this.getSession();
            int rows = sqlSession.insert(this.getNamespace(entity.getClass()) + "." + "insert", entity);
            return rows;
        } catch (Exception e) {
            e.printStackTrace();
            throw this.doTranslate("新增出错...", sqlSession, e);
        }
    }

    public int update(T entity) {
        final SqlSession sqlSession = this.getSession();
        final Class<?> entityClazz = entity.getClass();
        final Object entityObj = entity;

        try {
            TransactionTemplate transactionTemplate = new TransactionTemplate((PlatformTransactionManager) ApplicationContextManager.getContext().getBean("transactionManager"));
            int rows = (Integer)transactionTemplate.execute(new TransactionCallback<Integer>() {
                public Integer doInTransaction(TransactionStatus txStatus) {
                    int row = sqlSession.update(DefaultMybatisDao.this.getNamespace(entityClazz) + "." + "update", entityObj);
//                    if (OperationContextHolder.getContext().containsKey("nullValParams") && OperationContextHolder.getContext().get("nullValParams") instanceof Map) {
//                        sqlSession.update(DefaultMybatisDao.this.getNamespace(entityClazz) + "." + "updateNull", OperationContextHolder.getContext().get("nullValParams"));
//                    }

                    return row;
                }
            });
            return rows;
        } catch (Exception e) {
            e.printStackTrace();
            throw this.doTranslate("更新出错...", sqlSession, e);
        }
    }

    public int delete(Object entity) {
        SqlSession sqlSession = null;

        try {
            sqlSession = this.getSession();
            int rows;
            if (this.isLogicDelete(entity.getClass())) {
                rows = sqlSession.update(this.getNamespace(entity.getClass()) + "." + "deleteLogic", entity);
                return rows;
            } else {
                rows = sqlSession.delete(this.getNamespace(entity.getClass()) + "." + "delete", entity);
                return rows;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw this.doTranslate("删除出错...", sqlSession, e);
        }
    }

    public int count(Object entity) {
        SqlSession sqlSession = null;

        try {
            sqlSession = this.getSession();
            int rows = (Integer)sqlSession.selectOne(this.getNamespace(entity.getClass()) + "." + "count", entity);
            return rows;
        } catch (Exception e) {
            e.printStackTrace();
            throw this.doTranslate("统计出错...", sqlSession, e);
        }
    }

    public T selectByID(Object id) {
        SqlSession sqlSession = null;

        try {
            if (EntityBeanUtil.isEntityObj(id)) {
                id = PrimaryKeyUtil.getPK(id);
            }

            sqlSession = this.getSession();
            return sqlSession.selectOne(this.namespace + "." + "selectByID", id);
        } catch (Exception e) {
            e.printStackTrace();
            throw this.doTranslate("查询出错...", sqlSession, e);
        }
    }

    public T selectOne(T entity) {
        SqlSession sqlSession = null;

        try {
            sqlSession = this.getSession();
            List<T> resultList = sqlSession.selectList(this.getNamespace(entity.getClass()) + "." + "select", entity);
            return resultList != null && resultList.size() != 0 ? resultList.get(0) : null;
        } catch (Exception e) {
            e.printStackTrace();
            throw this.doTranslate("查询出错...", sqlSession, e);
        }
    }

    public List<T> selectAll() {
        SqlSession sqlSession = null;

        try {
            sqlSession = this.getSession();
            return sqlSession.selectList(this.namespace + "." + "selectAll");
        } catch (Exception e) {
            e.printStackTrace();
            throw this.doTranslate("查询出错...", sqlSession, e);
        }
    }

    public List<T> selectAll(Sortable sort) {
        SqlSession sqlSession = null;

        try {
            sqlSession = this.getSession();
            return sqlSession.selectList(this.namespace + "." + "selectAll", (Object)null, new RowBounds4DynSQL(sort, (Conditions)null, false));
        } catch (Exception e) {
            e.printStackTrace();
            throw this.doTranslate("查询出错...", sqlSession, e);
        }
    }

    public List<T> select(T condition) {
        SqlSession sqlSession = null;

        try {
            sqlSession = this.getSession();
            return sqlSession.selectList(this.getNamespace(condition.getClass()) + "." + "select", condition);
        } catch (Exception e) {
            e.printStackTrace();
            throw this.doTranslate("查询出错...", sqlSession, e);
        }
    }

    public List<T> select(T condition, Sortable sort) {
        SqlSession sqlSession = null;

        try {
            sqlSession = this.getSession();
            return sqlSession.selectList(this.getNamespace(condition.getClass()) + "." + "select", condition, new RowBounds4DynSQL(sort, (Conditions)null, false));
        } catch (Exception e) {
            e.printStackTrace();
            throw this.doTranslate("查询出错...", sqlSession, e);
        }
    }

    public List<T> selectMany(T[] conditions) {
        List<Object> list = new ArrayList();
        Map<String, Object> map = null;
        int i = 1;
        SqlSession sqlSession = null;

        try {
            Object[] objects = conditions;
            int length = conditions.length;

            for(int k = 0; k < length; ++k) {
                T condition = (T) objects[k];
                map = EntityBeanUtil.toMap(condition);
                map.put("tableAlias", "ak_temp_tableAlias_" + i++);
                list.add(map);
            }

            sqlSession = this.getSession();
            return sqlSession.selectList(this.getNamespace(list.get(0).getClass()) + "." + "selectMany", list);
        } catch (Exception e) {
            e.printStackTrace();
            throw this.doTranslate("多条件查询出错...", sqlSession, e);
        }
    }

    public List<T> selectMany(T[] conditions, Sortable sort) {
        List<Object> list = new ArrayList();
        Map<String, Object> map = null;
        int i = 1;
        SqlSession sqlSession = null;

        try {
            Object[] objects = conditions;
            int length = conditions.length;

            for(int k = 0; k < length; ++k) {
                T condition = (T) objects[k];
                map = EntityBeanUtil.toMap(condition);
                map.put("tableAlias", "ak_temp_tableAlias_" + i++);
                list.add(map);
            }

            sqlSession = this.getSession();
            return sqlSession.selectList(this.getNamespace(list.get(0).getClass()) + "." + "selectMany", list, new RowBounds4DynSQL(sort, (Conditions)null, false));
        } catch (Exception e) {
            e.printStackTrace();
            throw this.doTranslate("多条件查询出错...", sqlSession, e);
        }
    }

    public List<T> selectUnion(List<T> conditions) {
        SqlSession sqlSession = null;

        try {
            Class<?> clazz = Object.class;
            if (conditions != null && conditions.size() != 0) {
                clazz = conditions.get(0).getClass();
            }

            sqlSession = this.getSession();
            return sqlSession.selectList(this.getNamespace(clazz) + "." + "selectUnion", conditions);
        } catch (Exception e) {
            e.printStackTrace();
            throw this.doTranslate("多条件查询出错...", sqlSession, e);
        }
    }

    public List<T> selectUnion(List<T> conditions, Sortable sort) {
        SqlSession sqlSession = null;

        try {
            Class<?> clazz = Object.class;
            if (conditions != null && conditions.size() != 0) {
                clazz = conditions.get(0).getClass();
            }

            sqlSession = this.getSession();
            return sqlSession.selectList(this.getNamespace(clazz) + "." + "selectUnion", conditions, new RowBounds4DynSQL(sort, (Conditions)null, false));
        } catch (Exception e) {
            e.printStackTrace();
            throw this.doTranslate("多条件查询出错...", sqlSession, e);
        }
    }

    public int insertBatch(List<T> list) {
        SqlSession sqlSession = null;

        try {
            if (list != null && list.size() != 0) {
                PKWrapper wrapper;
                if ((wrapper = PKWrapperFactory.getPKWrapper(list.get(0))) != null) {
                    wrapper.wrapListEntityWithPK((List<Object>) list);
                }

                sqlSession = this.getSession();
                int rows = sqlSession.insert(this.getNamespace(list.get(0).getClass()) + "." + "insertBatch", list);
                return rows;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw this.doTranslate("批量新增出错...", sqlSession, e);
        }
    }

    public int updateBatch(List<T> list) {
        if (list != null && list.size() != 0) {
            final SqlSession sqlSession = this.getSession();
            final Class<?> entityClazz = list.get(0).getClass();
            final List list_ = list;

            try {
                TransactionTemplate transactionTemplate = new TransactionTemplate((PlatformTransactionManager) ApplicationContextManager.getContext().getBean("transactionManager"));
                int rows = (Integer)transactionTemplate.execute(new TransactionCallback<Integer>() {
                    public Integer doInTransaction(TransactionStatus txStatus) {
                        int row = 0;

                        Iterator iterator;
                        Object t;
                        for(iterator = list_.iterator(); iterator.hasNext(); row += sqlSession.update(DefaultMybatisDao.this.getNamespace(entityClazz) + "." + "update", t)) {
                            t = iterator.next();
                        }

//                        if (OperationContextHolder.getContext().containsKey("nullValParams") && OperationContextHolder.getContext().get("nullValParams") instanceof List) {
//                            iterator = ((List)OperationContextHolder.getContext().get("nullValParams")).iterator();
//
//                            while(iterator.hasNext()) {
//                                Map<String, Object> m = (Map)iterator.next();
//                                sqlSession.update(DefaultMybatisDao.this.getNamespace(entityClazz) + "." + "updateNull", m);
//                            }
//                        }

                        return row;
                    }
                });
                return rows;
            } catch (Exception e) {
                e.printStackTrace();
                throw this.doTranslate("批量更新出错...", sqlSession, e);
            }
        } else {
            return 0;
        }
    }

    public int deleteBatch(List<? extends Object> list) {
        if (list != null && list.size() != 0) {
            SqlSession sqlSession = null;

            try {
                sqlSession = this.getSession();
                int rows;
                if (this.isLogicDelete(list.get(0).getClass())) {
                    rows = sqlSession.update(this.getNamespace(list.get(0).getClass()) + "." + "deleteBatchLogic", list);
                    return rows;
                } else {
                    rows = sqlSession.delete(this.getNamespace(list.get(0).getClass()) + "." + "deleteBatch", list);
                    return rows;
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw this.doTranslate("批量删除出错...", sqlSession, e);
            }
        } else {
            return 0;
        }
    }

    public Page<T> selectByPage(Object entity, Pageable pageable, Sortable sortable, boolean isCount) {
        SqlSession sqlSession = null;

        try {
            int page = pageable == null ? 1 : pageable.getPageIndex();
            int size = pageable == null ? 15 : pageable.getPageSize();
            RowBounds4Page rowBounds = new RowBounds4Page((page - 1) * size, size, sortable, (Conditions)null, isCount);
            sqlSession = this.getSession();
            List<T> resultList = sqlSession.selectList(this.getNamespace(entity.getClass()) + "." + "selectByPage", entity, rowBounds);
            Page<T> pagingResult = new Page(page, size, isCount ? rowBounds.getTotalSize() : 0, resultList);
            return pagingResult;
        } catch (Exception e) {
            e.printStackTrace();
            throw this.doTranslate("查询出错...", sqlSession, e);
        }
    }

    public Page<T> selectPageByCondition(Object entity, Conditions condition, Pageable pageable, Sortable sortable, boolean isCount) {
        SqlSession sqlSession = null;

        try {
            int page = pageable == null ? 1 : pageable.getPageIndex();
            int size = pageable == null ? 15 : pageable.getPageSize();
            RowBounds4Page rowBounds = new RowBounds4Page((page - 1) * size, size, sortable, condition, isCount);
            sqlSession = this.getSession();
            List<T> resultList = sqlSession.selectList(this.getNamespace(entity.getClass()) + "." + "selectByPage", entity, rowBounds);
            Page<T> pagingResult = new Page(page, size, isCount ? rowBounds.getTotalSize() : 0, resultList);
            return pagingResult;
        } catch (Exception e) {
            e.printStackTrace();
            throw this.doTranslate("查询出错...", sqlSession, e);
        }
    }

    public List<T> selectByCondition(Conditions condition, Sortable sortable) {
        SqlSession sqlSession = null;

        try {
            RowBounds4DynSQL rowBounds = new RowBounds4DynSQL(sortable, condition, false);
            sqlSession = this.getSession();
            return sqlSession.selectList(this.getNamespace() + "." + "select", Maps.newHashMap(), rowBounds);
        } catch (Exception e) {
            e.printStackTrace();
            throw this.doTranslate("查询出错...", sqlSession, e);
        }
    }

    public List<T> selectByCondition(String sql, Conditions condition, Sortable sortable) {
        SqlSession sqlSession = null;

        try {
            RowBounds4DynSQL rowBounds = new RowBounds4DynSQL(sortable, condition, false);
            sqlSession = this.getSession();
            return sqlSession.selectList(sql, Maps.newHashMap(), rowBounds);
        } catch (Exception e) {
            e.printStackTrace();
            throw this.doTranslate("查询出错...", sqlSession, e);
        }
    }

    public SqlSession getSession() {
        DynamicDataSource.setDataSourceKey(this.dataSourceID);
        return this.getSqlSession();
    }

    public void setDataSource(String datasourceid) {
        this.dataSourceID = datasourceid;
    }

    private AkDaoException doTranslate(String msg, SqlSession sqlSession, Exception e) {
        try {
            return AkDaoExceptionTranslator.doTranslate(msg, e, sqlSession != null ? sqlSession.getConfiguration().getEnvironment().getDataSource().getConnection().getMetaData().getDatabaseProductName().toLowerCase() : "");
        } catch (SQLException ex) {
            return new AkDaoException(msg, e);
        }
    }

    public void setLogicDelete(boolean logicDelete) {
        this.logicDelete = logicDelete;
    }

    private boolean isLogicDelete(Class<?> entityClass) {
        Entity entity = null;
        if (this.logicDelete != null) {
            return this.logicDelete;
        } else {
            return (entity = (Entity)entityClass.getAnnotation(Entity.class)) != null ? entity.logicDelete() : false;
        }
    }

    public void printLog(String sqlId, Object param) {
        Configuration configuration = this.getSession().getConfiguration();
        MappedStatement mappedStatement = configuration.getMappedStatement(sqlId);
        BoundSql boundSql = mappedStatement.getBoundSql(param);
        String sql = boundSql.getSql().trim();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("info-sql: " + sdf.format(new Date()) + "  " + sql);
    }
}
