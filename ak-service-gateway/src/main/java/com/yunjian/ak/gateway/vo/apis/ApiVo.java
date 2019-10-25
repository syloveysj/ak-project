package com.yunjian.ak.gateway.vo.apis;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/19 21:16
 * @Version 1.0
 */
@Data
public class ApiVo {
    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "别名")
    private String alias;

    @ApiModelProperty(value = "备注")
    private String memo;

    @ApiModelProperty(value = "方法")
    private String method;

    @ApiModelProperty(value = "路径")
    private String uri;

    @ApiModelProperty(value = "状态(0：可导入，1：可更新，2：已存在)")
    private Integer state;
}
