package com.yunjian.ak.gateway.vo.apis;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/19 21:18
 * @Version 1.0
 */
@Data
public class ApiPackageVo {
    @ApiModelProperty(value = "服务ID")
    @NotBlank(message = "服务ID不能为空")
    private String serverId;
    @ApiModelProperty(value = "api列表")
    private List<ApiVo> rows;
    @ApiModelProperty(value = "json文本")
    private String jsonText;
}
