package com.yunjian.ak.gateway.entity.apis;

import com.yunjian.ak.dao.annotation.Column;
import com.yunjian.ak.dao.annotation.ColumnType;
import com.yunjian.ak.dao.annotation.Entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/8 22:22
 * @Version 1.0
 */
@Entity(
        id = "entity:com.yunjian.ak.gateway.entity.apis.TargetEntity",
        table = "targets",
        ds = "apis",
        cache = false
)
@Data
public class TargetEntity {
    @Column( id = "id", type = ColumnType.custom )
    @ApiModelProperty(value = "ID")
    private String id;

    @Column( id = "target" )
    @ApiModelProperty(value = "目标地址")
    @NotBlank(message = "目标地址不能为空")
    private String target;

    @Column( id = "weight" )
    @ApiModelProperty(value = "权重")
    private Integer weight;

    @ApiModelProperty(value = "标签")
    private List<String> tags;
}
