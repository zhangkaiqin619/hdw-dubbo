package com.hdw.sms.dto;

import com.hdw.common.mybatis.base.dto.CommonDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description com.hdw.sms.param
 * @Author TuMingLong
 * @Date 2019/11/7 10:10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("查询消息类型参数对象")
public class SmsTypeDTO extends CommonDTO {

    @ApiModelProperty("类型名称")
    private String typeName;
}
