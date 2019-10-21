package com.yunjian.ak.gateway.service.router;

import com.alibaba.fastjson.JSON;
import com.yunjian.ak.dao.core.DaoFactory;
import com.yunjian.ak.dao.mybatis.enhance.Page;
import com.yunjian.ak.dao.mybatis.enhance.Pageable;
import com.yunjian.ak.dao.mybatis.enhance.Sortable;
import com.yunjian.ak.dao.utils.SortableUtil;
import com.yunjian.ak.gateway.entity.router.ServiceEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/8 23:25
 * @Version 1.0
 */
@Service
public class ServiceService {
    public ServiceService() {
        super();
    }

    public ServiceEntity update(ServiceEntity entity) {
        DaoFactory.create(ServiceEntity.class).update(entity);
        return entity;
    }

    public ServiceEntity get(String id) {
        return DaoFactory.create(ServiceEntity.class).selectByID(id);
    }

    public List<ServiceEntity> getAll() {
        return DaoFactory.create(ServiceEntity.class).selectAll();
    }

    public Page<ServiceEntity> getListByPage(int page, int pagesize, String sort, String order, String cond) {
        Pageable pageable = new Pageable(page, pagesize);
        Sortable sortable = SortableUtil.getSortable(sort, order);
        ServiceEntity entity = StringUtils.isNotBlank(cond) ? JSON.parseObject(cond, ServiceEntity.class) : new ServiceEntity();
        return DaoFactory.create(ServiceEntity.class).selectByPage(entity, pageable, sortable, true);
    }
}
