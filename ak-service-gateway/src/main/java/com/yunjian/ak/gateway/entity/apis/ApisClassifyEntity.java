package com.yunjian.ak.gateway.entity.apis;

import com.yunjian.ak.dao.annotation.Column;
import com.yunjian.ak.dao.annotation.ColumnType;
import com.yunjian.ak.dao.annotation.Entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/21 23:21
 * @Version 1.0
 */
@Entity(
        id = "entity:com.yunjian.ak.gateway.entity.apis.ApisClassifyEntity",
        table = "apis_classify",
        ds = "apis",
        cache = false
)
@Data
public class ApisClassifyEntity {
    @Column( id = "id", type = ColumnType.custom )
    @ApiModelProperty(value = "ID")
    private String id;

    @Column( id = "pid" )
    @ApiModelProperty(value = "上级节点ID")
    private String pid;

    @Column( id = "service_id" )
    @ApiModelProperty(value = "所属应用ID")
    @NotBlank(message = "所属应用不能为空")
    private String serviceId;

    @Column( id = "alias" )
    @ApiModelProperty(value = "服务分类名称")
    @NotBlank(message = "服务分类名称不能为空")
    private String alias;

    @Column( id = "memo" )
    @ApiModelProperty(value = "备注")
    private String memo;

    @Column( id = "created_at" )
    @ApiModelProperty(value = "创建时间")
    private Date createdAt;
}
