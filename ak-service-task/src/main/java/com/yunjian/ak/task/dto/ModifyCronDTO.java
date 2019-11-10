package com.yunjian.ak.task.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author: yong.sun
 * @Date: 2019/11/10 18:34
 * @Version 1.0
 */
@Data
public class ModifyCronDTO {
    @NotNull(message = "the job id cannot be null")
    private Integer id;

    @NotEmpty(message = "the cron cannot be empty")
    private String cron;
}
