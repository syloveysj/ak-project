package com.yunjian.ak.gateway.service.apis;

import com.alibaba.fastjson.JSON;
import com.yunjian.ak.dao.core.DaoFactory;
import com.yunjian.ak.dao.mybatis.enhance.Page;
import com.yunjian.ak.dao.mybatis.enhance.Pageable;
import com.yunjian.ak.dao.mybatis.enhance.Sortable;
import com.yunjian.ak.dao.utils.SortableUtil;
import com.yunjian.ak.gateway.entity.apis.ApplicationTypeEntity;
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
public class ApplicationTypeService {
    public ApplicationTypeService() {
        super();
    }

    public ApplicationTypeEntity insert(ApplicationTypeEntity entity) {
        DaoFactory.create(ApplicationTypeEntity.class).insert(entity);
        return entity;
    }

    public int insertBatch(List<ApplicationTypeEntity> list) {
        return DaoFactory.create(ApplicationTypeEntity.class).insertBatch(list);
    }

    public ApplicationTypeEntity update(ApplicationTypeEntity entity) {
        DaoFactory.create(ApplicationTypeEntity.class).update(entity);
        return entity;
    }

    public int updateBatch(List<ApplicationTypeEntity> list) {
        return DaoFactory.create(ApplicationTypeEntity.class).updateBatch(list);
    }

    public void delete(String id) {
        ApplicationTypeEntity entity = new ApplicationTypeEntity();
        entity.setId(id);
        DaoFactory.create(ApplicationTypeEntity.class).delete(entity);
    }

    public int deleteBatch(List<String> ids) {
        return DaoFactory.create(ApplicationTypeEntity.class).deleteBatch(ids);
    }

    public ApplicationTypeEntity get(String id) {
        return DaoFactory.create(ApplicationTypeEntity.class).selectByID(id);
    }

    public List<ApplicationTypeEntity> getAll() {
        return DaoFactory.create(ApplicationTypeEntity.class).selectAll();
    }

    public Page<ApplicationTypeEntity> getListByPage(int page, int pagesize, String sort, String order, String cond) {
        Pageable pageable = new Pageable(page, pagesize);
        Sortable sortable = SortableUtil.getSortable(sort, order);
        ApplicationTypeEntity entity = StringUtils.isNotBlank(cond) ? JSON.parseObject(cond, ApplicationTypeEntity.class) : new ApplicationTypeEntity();
        return DaoFactory.create(ApplicationTypeEntity.class).selectByPage(entity, pageable, sortable, true);
    }
}
