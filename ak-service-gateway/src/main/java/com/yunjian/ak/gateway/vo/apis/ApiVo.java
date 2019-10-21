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
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "方法")
    private String method;
    @ApiModelProperty(value = "路径")
    private String uri;
    @ApiModelProperty(value = "状态(0：可导入，1：可更新)")
    private Integer state;
}