package com.yunjian.ak.gateway.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/19 21:18
 * @Version 1.0
 */
@Data
public class ApiPackageVo {
    @ApiModelProperty(value = "api列表")
    private List<ApiVo> data;
    @ApiModelProperty(value = "json文本")
    private String jsonText;
}
