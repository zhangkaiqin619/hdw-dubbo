package com.hdw.enterprise.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hdw.enterprise.entity.EnterpriseDepartment;

import java.util.List;
import java.util.Map;

/**
 * 企业部门表
 *
 * @author TuMinglong
 * @date 2018-12-11 11:36:02
 */
public interface EnterpriseDepartmentMapper extends BaseMapper<EnterpriseDepartment> {
    /**
     * 树表
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> selectTreeGrid(Map<String, Object> params);

    /**
     * 自定义查询
     *
     * @param params
     * @return
     */
    List<EnterpriseDepartment> selectEnterpriseDepartmentList(Map<String, Object> params);
}
