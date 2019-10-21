package com.yunjian.ak.gateway.entity.apis;

import com.yunjian.ak.dao.annotation.Column;
import com.yunjian.ak.dao.annotation.ColumnType;
import com.yunjian.ak.dao.annotation.Entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/21 23:22
 * @Version 1.0
 */
@Entity(
        id = "entity:com.yunjian.ak.gateway.entity.apis.SwaggerEntity",
        table = "swagger",
        ds = "apis",
        cache = false
)
@Data
public class SwaggerEntity {
    @Column( id = "id", type = ColumnType.custom )
    @ApiModelProperty(value = "ID")
    private String id;

    @Column( id = "content" )
    @ApiModelProperty(value = "文档")
    private String content;
}
