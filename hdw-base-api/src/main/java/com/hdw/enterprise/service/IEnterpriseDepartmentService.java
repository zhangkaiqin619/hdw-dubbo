package com.hdw.enterprise.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hdw.enterprise.entity.EnterpriseDepartment;
import com.hdw.enterprise.vo.EnterpriseDepartmentVo;
import com.hdw.enterprise.dto.EnterpriseDepartmentDTO;

import java.util.List;
import java.util.Map;

/**
 * 企业部门表
 *
 * @author TuMinglong
 * @date 2018-12-11 11:36:02
 */
public interface IEnterpriseDepartmentService extends IService<EnterpriseDepartment> {

    /**
     * 树表
     *
     * @param enterpriseDepartmentDTO
     * @return
     */
    List<EnterpriseDepartmentVo> selectTreeGrid(EnterpriseDepartmentDTO enterpriseDepartmentDTO);

    /**
     * 自定义查询
     *
     * @param params
     * @return
     */
    List<EnterpriseDepartment> selectEnterpriseDepartmentList(Map<String, Object> params);
}

