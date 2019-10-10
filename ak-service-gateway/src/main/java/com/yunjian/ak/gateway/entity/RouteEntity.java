package com.yunjian.ak.gateway.entity;

import com.yunjian.ak.dao.annotation.Column;
import com.yunjian.ak.dao.annotation.ColumnType;
import com.yunjian.ak.dao.annotation.Entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/8 22:23
 * @Version 1.0
 */
@Entity(
        id = "entity:com.yunjian.ak.gateway.entity.RouteEntity",
        table = "routes",
        ds = "sys",
        cache = false
)
@Data
public class RouteEntity {
    @Column( id = "id", type = ColumnType.custom )
    @ApiModelProperty(value = "ID")
    private String id;

    @Column( id = "name" )
    @ApiModelProperty(value = "路由名称")
    private String name;

    @Column( id = "protocols" )
    @ApiModelProperty(value = "允许的协议列表")
    private List<String> protocols;

    @Column( id = "methods" )
    @ApiModelProperty(value = "匹配的HTTP方法的列表")
    private List<String> methods;

    @Column( id = "hosts" )
    @ApiModelProperty(value = "匹配的域名列表")
    private List<String> hosts;

    @Column( id = "paths" )
    @ApiModelProperty(value = "匹配的路径列表")
    private List<String> paths;

    @Column( id = "strip_path" )
    @ApiModelProperty(value = "请求网址中删除匹配的前缀")
    private Boolean stripPath;

    @Column( id = "preserve_host" )
    @ApiModelProperty(value = "使用请求头")
    private Boolean preserveHost;

    @Column( id = "alias" )
    @ApiModelProperty(value = "别名")
    private String alias;

    @Column( id = "created_at" )
    @ApiModelProperty(value = "创建时间")
    private Date createdAt;

    @Column( id = "updated_at" )
    @ApiModelProperty(value = "更新时间")
    private Date updatedAt;

    @ApiModelProperty(value = "标签")
    private List<String> tags;

    @ApiModelProperty(value = "关联的服务")
    private ServiceEntity service;
}
