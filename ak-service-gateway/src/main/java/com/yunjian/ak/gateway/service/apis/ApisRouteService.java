package com.yunjian.ak.gateway.service.apis;

import com.alibaba.fastjson.JSON;
import com.yunjian.ak.dao.core.DaoFactory;
import com.yunjian.ak.dao.mybatis.enhance.Page;
import com.yunjian.ak.dao.mybatis.enhance.Pageable;
import com.yunjian.ak.dao.mybatis.enhance.Sortable;
import com.yunjian.ak.dao.utils.SortableUtil;
import com.yunjian.ak.gateway.entity.apis.RouteEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/8 23:24
 * @Version 1.0
 */
@Service
public class ApisRouteService {
    public ApisRouteService() {
        super();
    }

    public RouteEntity update(RouteEntity entity) {
        DaoFactory.create(RouteEntity.class).update(entity);
        return entity;
    }

    public int updateBatch(List<RouteEntity> list) {
        return DaoFactory.create(RouteEntity.class).updateBatch(list);
    }

    public int updateClassifyBatch(String classifyId, List<String> ids) {
        Map<String, Object> map = new HashMap<>();
        map.put("classifyId", classifyId);
        map.put("list", ids);
        return DaoFactory.create(RouteEntity.class).getSession().update("com.yunjian.ak.gateway.entity.apis.RouteEntityMapper.updateClassifyBatch", map);
    }

    public RouteEntity get(String id) {
        return DaoFactory.create(RouteEntity.class).selectByID(id);
    }

    public List<RouteEntity> getAll() {
        return DaoFactory.create(RouteEntity.class).selectAll();
    }

    public Page<RouteEntity> getListByPage(int page, int pagesize, String sort, String order, String cond) {
        Pageable pageable = new Pageable(page, pagesize);
        Sortable sortable = SortableUtil.getSortable(sort, order);
        RouteEntity entity = StringUtils.isNotBlank(cond) ? JSON.parseObject(cond, RouteEntity.class) : new RouteEntity();
        return DaoFactory.create(RouteEntity.class).selectByPage(entity, pageable, sortable, true);
    }
}
