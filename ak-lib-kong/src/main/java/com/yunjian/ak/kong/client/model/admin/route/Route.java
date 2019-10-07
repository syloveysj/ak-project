package com.yunjian.ak.kong.client.model.admin.route;

import com.google.gson.annotations.SerializedName;
import com.yunjian.ak.kong.client.model.admin.service.Service;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/9/26 15:50
 * @Version 1.0
 */
@Data
public class Route {

    private String id;
    private String name;
    private List<String> protocols;
    private List<String> methods;
    private List<String> hosts;
    private List<String> paths;
    @SerializedName("strip_path")
    private Boolean stripPath;
    @SerializedName("preserve_host")
    private Boolean preserveHost;
    private Service service;
    @SerializedName("created_at")
    private Long createdAt;
    @SerializedName("updated_at")
    private Long updatedAt;
}
