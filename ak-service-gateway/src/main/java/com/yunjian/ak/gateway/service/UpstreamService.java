package com.yunjian.ak.gateway.service;

import com.alibaba.fastjson.JSON;
import com.yunjian.ak.dao.core.DaoFactory;
import com.yunjian.ak.dao.mybatis.enhance.Page;
import com.yunjian.ak.dao.mybatis.enhance.Pageable;
import com.yunjian.ak.dao.mybatis.enhance.Sortable;
import com.yunjian.ak.dao.utils.SortableUtil;
import com.yunjian.ak.gateway.entity.UpstreamEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/6 0:27
 * @Version 1.0
 */
@Service
public class UpstreamService {
    public UpstreamService() {
        super();
    }

    public UpstreamEntity update(UpstreamEntity entity) {
        DaoFactory.create(UpstreamEntity.class).update(entity);
        return entity;
    }

    public UpstreamEntity get(String id) {
        return DaoFactory.create(UpstreamEntity.class).selectByID(id);
    }

    public List<UpstreamEntity> getAll() {
        return DaoFactory.create(UpstreamEntity.class).selectAll();
    }

    public Page<UpstreamEntity> getListByPage(int page, int pagesize, String sort, String order, String cond) {
        Pageable pageable = new Pageable(page, pagesize);
        Sortable sortable = SortableUtil.getSortable(sort, order);
        UpstreamEntity entity = StringUtils.isNotBlank(cond) ? JSON.parseObject(cond, UpstreamEntity.class) : new UpstreamEntity();
        return DaoFactory.create(UpstreamEntity.class).selectByPage(entity, pageable, sortable, true);
    }
}
