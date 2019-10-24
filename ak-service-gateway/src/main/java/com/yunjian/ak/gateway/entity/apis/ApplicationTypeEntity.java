package com.yunjian.ak.gateway.entity.apis;

import com.yunjian.ak.dao.annotation.Column;
import com.yunjian.ak.dao.annotation.ColumnType;
import com.yunjian.ak.dao.annotation.Entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/21 23:21
 * @Version 1.0
 */
@Entity(
        id = "entity:com.yunjian.ak.gateway.entity.apis.ApplicationTypeEntity",
        table = "application_type",
        ds = "apis",
        cache = false
)
@Data
public class ApplicationTypeEntity {
    @Column( id = "id", type = ColumnType.custom )
    @ApiModelProperty(value = "ID")
    private Integer id;

    @Column( id = "type_name" )
    @ApiModelProperty(value = "应用分类名称")
    @NotBlank(message = "应用分类名称不能为空")
    private String typeName;
}
