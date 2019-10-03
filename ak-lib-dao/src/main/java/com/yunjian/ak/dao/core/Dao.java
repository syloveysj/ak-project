package com.yunjian.ak.dao.core;

import com.yunjian.ak.dao.mybatis.enhance.Conditions;
import com.yunjian.ak.dao.mybatis.enhance.Page;
import com.yunjian.ak.dao.mybatis.enhance.Pageable;
import com.yunjian.ak.dao.mybatis.enhance.Sortable;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/5/24 12:16
 * @Version 1.0
 */
public interface Dao<T> {
    void setNamespace(String namespace);

    void setDataSource(String dataSource);

    void setLogicDelete(boolean logicDelete);

    int insert(T entity);

    int update(T entity);

    int delete(Object o);

    int count(Object o);

    T selectOne(T entity);

    List<T> selectAll();

    T selectByID(Object o);

    List<T> selectAll(Sortable sortable);

    List<T> select(T entity);

    List<T> select(T entity, Sortable sortable);

    /** @deprecated */
    @Deprecated
    List<T> selectMany(T[] conditions);

    /** @deprecated */
    @Deprecated
    List<T> selectMany(T[] conditions, Sortable sortable);

    List<T> selectUnion(List<T> list);

    List<T> selectUnion(List<T> list, Sortable sortable);

    Page<T> selectByPage(Object o, Pageable pageable, Sortable sortable, boolean isCount);

    Page<T> selectPageByCondition(Object o, Conditions conditions, Pageable pageable, Sortable sortable, boolean isCount);

    int insertBatch(List<T> list);

    int updateBatch(List<T> list);

    int deleteBatch(List<? extends Object> list);

    List<T> selectByCondition(Conditions conditions, Sortable sortable);

    List<T> selectByCondition(String sql, Conditions conditions, Sortable sortable);

    SqlSession getSession();
}
