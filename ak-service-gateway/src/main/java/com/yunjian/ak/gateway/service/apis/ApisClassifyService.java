package com.yunjian.ak.gateway.service.apis;

import com.alibaba.fastjson.JSON;
import com.yunjian.ak.dao.core.DaoFactory;
import com.yunjian.ak.dao.mybatis.enhance.Page;
import com.yunjian.ak.dao.mybatis.enhance.Pageable;
import com.yunjian.ak.dao.mybatis.enhance.Sortable;
import com.yunjian.ak.dao.utils.SortableUtil;
import com.yunjian.ak.gateway.entity.apis.ApisClassifyEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/22 0:19
 * @Version 1.0
 */
@Service
public class ApisClassifyService {
    public ApisClassifyService() {
        super();
    }

    public ApisClassifyEntity insert(ApisClassifyEntity entity) {
        DaoFactory.create(ApisClassifyEntity.class).insert(entity);
        return entity;
    }

    public int insertBatch(List<ApisClassifyEntity> list) {
        return DaoFactory.create(ApisClassifyEntity.class).insertBatch(list);
    }

    public ApisClassifyEntity update(ApisClassifyEntity entity) {
        DaoFactory.create(ApisClassifyEntity.class).update(entity);
        return entity;
    }

    public int updateBatch(List<ApisClassifyEntity> list) {
        return DaoFactory.create(ApisClassifyEntity.class).updateBatch(list);
    }

    public void delete(String id) {
        ApisClassifyEntity entity = new ApisClassifyEntity();
        entity.setId(id);
        DaoFactory.create(ApisClassifyEntity.class).delete(entity);
    }

    public int deleteBatch(List<String> ids) {
        return DaoFactory.create(ApisClassifyEntity.class).deleteBatch(ids);
    }

    public ApisClassifyEntity get(String id) {
        return DaoFactory.create(ApisClassifyEntity.class).selectByID(id);
    }

    public List<ApisClassifyEntity> getAll() {
        return DaoFactory.create(ApisClassifyEntity.class).selectAll();
    }

    public Page<ApisClassifyEntity> getListByPage(int page, int pagesize, String sort, String order, String cond) {
        Pageable pageable = new Pageable(page, pagesize);
        Sortable sortable = SortableUtil.getSortable(sort, order);
        ApisClassifyEntity entity = StringUtils.isNotBlank(cond) ? JSON.parseObject(cond, ApisClassifyEntity.class) : new ApisClassifyEntity();
        return DaoFactory.create(ApisClassifyEntity.class).selectByPage(entity, pageable, sortable, true);
    }
}
