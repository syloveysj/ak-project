package com.yunjian.ak.gateway.vo.apis;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/10/19 22:06
 * @Version 1.0
 */
@Data
public class ApiAnalysisVo {
    @ApiModelProperty(value = "服务ID")
    @NotBlank(message = "服务ID不能为空")
    private String serverId;
    @ApiModelProperty(value = "服务json地址")
    private String jsonUrl;
    @ApiModelProperty(value = "服务json文本")
    private String jsonText;
}
