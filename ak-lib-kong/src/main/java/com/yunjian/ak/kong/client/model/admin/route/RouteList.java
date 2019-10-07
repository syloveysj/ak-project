package com.yunjian.ak.kong.client.model.admin.route;

import com.yunjian.ak.kong.client.model.common.AbstractEntityList;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/9/26 15:51
 * @Version 1.0
 */
@Data
public class RouteList extends AbstractEntityList {
    Long total;
    String next;
    List<Route> data;
}
