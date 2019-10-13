package com.yunjian.ak.gateway.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunjian.ak.dao.annotation.Column;
import com.yunjian.ak.dao.annotation.ColumnType;
import com.yunjian.ak.dao.annotation.Entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/4 0:18
 * @Version 1.0
 */
@Entity(
        id = "entity:com.yunjian.ak.gateway.entity.UpstreamEntity",
        table = "upstreams",
        ds = "sys",
        cache = false
)
@Data
public class UpstreamEntity {
    @Column( id = "id", type = ColumnType.custom )
    @ApiModelProperty(value = "ID")
    private String id;

    @Column( id = "name" )
    @ApiModelProperty(value = "主机名")
    @NotBlank(message = "主机名不能为空")
    private String name;

    @Column( id = "algorithm" )
    @ApiModelProperty(value = "负载均衡算法")
    private String algorithm;

    @Column( id = "slots" )
    @ApiModelProperty(value = "负载均衡算法中的权重")
    private Integer slots;

    @Column( id = "alias" )
    @ApiModelProperty(value = "别名")
    private String alias;

    @Column( id = "created_at" )
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date createdAt;

    @ApiModelProperty(value = "标签")
    private List<String> tags;

    @ApiModelProperty(value = "目标地址列表")
    private List<TargetEntity> targets;

    @Column( id = "targets_memo" )
    private String targetsMemo;
}
