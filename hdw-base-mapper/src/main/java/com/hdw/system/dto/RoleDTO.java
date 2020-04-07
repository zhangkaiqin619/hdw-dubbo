package com.hdw.system.dto;

import com.hdw.common.mybatis.base.dto.CommonDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 查询角色对象
 * @Author TuMingLong
 * @Date 2019/11/6 11:43
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("查询角色对象")
public class RoleDTO extends CommonDTO {

    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "创建者ID", hidden = true)
    private Long createUserId;


}
