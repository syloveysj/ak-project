package com.yunjian.ak.gateway.entity.apis;

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
        id = "entity:com.yunjian.ak.gateway.entity.apis.ServiceEntity",
        table = "services",
        ds = "apis",
        cache = false
)
@Data
public class ServiceEntity {
    @Column( id = "id", type = ColumnType.custom )
    @ApiModelProperty(value = "ID")
    private String id;

    @Column( id = "name" )
    @ApiModelProperty(value = "服务名称")
    private String name;

    @Column( id = "protocol" )
    @ApiModelProperty(value = "通信的协议")
    private String protocol;

    @Column( id = "host" )
    @ApiModelProperty(value = "服务器主机")
    private String host;

    @Column( id = "port" )
    @ApiModelProperty(value = "服务器端口")
    private Integer port;

    @Column( id = "path" )
    @ApiModelProperty(value = "服务器请求路径")
    private String path;

    @Column( id = "url" )
    @ApiModelProperty(value = "速记属性来设置protocol，host，port和path一次")
    private String url;

    @Column( id = "retries" )
    @ApiModelProperty(value = "代理失败后要执行的重试次数")
    private Integer retries;

    @Column( id = "connect_timeout" )
    @ApiModelProperty(value = "连接的超时时间")
    private Long connectTimeout;

    @Column( id = "read_timeout" )
    @ApiModelProperty(value = "两次连续读取操作之间的超时时间")
    private Long readTimeout;

    @Column( id = "write_timeout" )
    @ApiModelProperty(value = "两个连续写操作之间的超时时间")
    private Long writeTimeout;

    @Column( id = "alias" )
    @ApiModelProperty(value = "别名")
    private String alias;

    @Column( id = "created_at" )
    @ApiModelProperty(value = "创建时间")
    private Date createdAt;

    @Column( id = "updated_at" )
    @ApiModelProperty(value = "更新时间")
    private Date updatedAt;

    @Column( id = "memo" )
    @ApiModelProperty(value = "备注")
    private String memo;

    @Column( id = "type_id" )
    @ApiModelProperty(value = "分类ID")
    private Integer typeId;

    @ApiModelProperty(value = "路由列表")
    private List<RouteEntity> routes;
}
