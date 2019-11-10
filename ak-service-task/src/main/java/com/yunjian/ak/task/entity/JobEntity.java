package com.yunjian.ak.task.entity;

import com.yunjian.ak.dao.annotation.Column;
import com.yunjian.ak.dao.annotation.ColumnType;
import com.yunjian.ak.dao.annotation.Entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * job_entity 实体类(个人示例,可自定义相关属性)
 *
 * @author
 *
 */
@Entity(
        id = "entity:com.yunjian.ak.task.entity.JobEntity",
        table = "job_entity",
        ds = "sys",
        cache = false
)
@Data
@Accessors(chain = true)
public class JobEntity implements Serializable {
    @Column( id = "id", type = ColumnType.custom )
    @ApiModelProperty(value = "id")
    private Integer id;

    @Column( id = "name" )
    @ApiModelProperty(value = "job名称")
    private String name;

    @Column( id = "job_group" )
    @ApiModelProperty(value = "job组名")
    private String jobGroup;

    @Column( id = "cron" )
    @ApiModelProperty(value = "执行的cron")
    private String cron;

    @Column( id = "parameter" )
    @ApiModelProperty(value = "job的参数")
    private String parameter;

    @Column( id = "description" )
    @ApiModelProperty(value = "job描述信息")
    private String description;

    @Column( id = "vm_param" )
    @ApiModelProperty(value = "vm参数")
    private String vmParam;

    @Column( id = "jar_path" )
    @ApiModelProperty(value = "job的jar路径")
    private String jarPath;

    @Column( id = "status" )
    @ApiModelProperty(value = "job的执行状态,设置为OPEN/CLOSE且只有该值为OPEN才会执行该Job")
    private String status;

}
