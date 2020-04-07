package com.hdw.job.dto;

import com.hdw.common.mybatis.base.dto.CommonDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 查询定时任务日志参数对象
 * @Author TuMingLong
 * @Date 2019/11/6 18:57
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("查询定时任务日志参数对象")
public class JobLogDTO extends CommonDTO {

    @ApiModelProperty(value = "定时任务ID")
    private String jobId;
}
