package com.yunjian.ak.gateway.service.router;

import com.alibaba.fastjson.JSON;
import com.yunjian.ak.dao.core.DaoFactory;
import com.yunjian.ak.dao.mybatis.enhance.Page;
import com.yunjian.ak.dao.mybatis.enhance.Pageable;
import com.yunjian.ak.dao.mybatis.enhance.Sortable;
import com.yunjian.ak.dao.utils.SortableUtil;
import com.yunjian.ak.gateway.entity.router.TargetEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/8 23:21
 * @Version 1.0
 */
@Service
public class TargetService {
    public TargetService() {
        super();
    }

    public List<TargetEntity> getAll() {
        return DaoFactory.create(TargetEntity.class).selectAll();
    }

    public Page<TargetEntity> getListByPage(int page, int pagesize, String sort, String order, String cond) {
        Pageable pageable = new Pageable(page, pagesize);
        Sortable sortable = SortableUtil.getSortable(sort, order);
        TargetEntity entity = StringUtils.isNotBlank(cond) ? JSON.parseObject(cond, TargetEntity.class) : new TargetEntity();
        return DaoFactory.create(TargetEntity.class).selectByPage(entity, pageable, sortable, true);
    }
}
