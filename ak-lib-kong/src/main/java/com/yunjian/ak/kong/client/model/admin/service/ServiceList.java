package com.yunjian.ak.kong.client.model.admin.service;

import com.yunjian.ak.kong.client.model.common.AbstractEntityList;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/9/26 15:46
 * @Version 1.0
 */
@Data
public class ServiceList extends AbstractEntityList {
    Long total;
    String next;
    List<Service> data;
}
