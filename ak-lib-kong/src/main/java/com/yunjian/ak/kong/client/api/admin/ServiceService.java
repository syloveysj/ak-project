package com.yunjian.ak.kong.client.api.admin;

import com.yunjian.ak.kong.client.model.admin.service.Service;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/9/26 15:48
 * @Version 1.0
 */
public interface ServiceService {

    Service createService(Service service);

    Service updateService(String nameOrId, Service service);

    void deleteService(String nameOrId);
}
