package com.yunjian.ak.gateway.service.apis;

import com.alibaba.fastjson.JSON;
import com.yunjian.ak.dao.core.DaoFactory;
import com.yunjian.ak.dao.mybatis.enhance.Page;
import com.yunjian.ak.dao.mybatis.enhance.Pageable;
import com.yunjian.ak.dao.mybatis.enhance.Sortable;
import com.yunjian.ak.dao.utils.SortableUtil;
import com.yunjian.ak.gateway.entity.apis.SwaggerEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/22 0:20
 * @Version 1.0
 */
@Service
public class SwaggerService {
    public SwaggerService() {
        super();
    }

    public SwaggerEntity insert(SwaggerEntity entity) {
        DaoFactory.create(SwaggerEntity.class).insert(entity);
        return entity;
    }

    public int insertBatch(List<SwaggerEntity> list) {
        return DaoFactory.create(SwaggerEntity.class).insertBatch(list);
    }

    public SwaggerEntity update(SwaggerEntity entity) {
        DaoFactory.create(SwaggerEntity.class).update(entity);
        return entity;
    }

    public int updateBatch(List<SwaggerEntity> list) {
        return DaoFactory.create(SwaggerEntity.class).updateBatch(list);
    }

    public void delete(String id) {
        SwaggerEntity entity = new SwaggerEntity();
        entity.setId(id);
        DaoFactory.create(SwaggerEntity.class).delete(entity);
    }

    public int deleteBatch(List<String> ids) {
        return DaoFactory.create(SwaggerEntity.class).deleteBatch(ids);
    }

    public SwaggerEntity get(String id) {
        return DaoFactory.create(SwaggerEntity.class).selectByID(id);
    }

    public List<SwaggerEntity> getAll() {
        return DaoFactory.create(SwaggerEntity.class).selectAll();
    }

    public Page<SwaggerEntity> getListByPage(int page, int pagesize, String sort, String order, String cond) {
        Pageable pageable = new Pageable(page, pagesize);
        Sortable sortable = SortableUtil.getSortable(sort, order);
        SwaggerEntity entity = StringUtils.isNotBlank(cond) ? JSON.parseObject(cond, SwaggerEntity.class) : new SwaggerEntity();
        return DaoFactory.create(SwaggerEntity.class).selectByPage(entity, pageable, sortable, true);
    }
}
