package com.hdw.enterprise.mapper;

import com.hdw.common.mybatis.base.mapper.SuperMapper;
import com.hdw.enterprise.entity.EnterpriseJob;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 企业职务配置表
 *
 * @author TuMinglong
 * @date 2018-12-11 11:36:02
 */
public interface EnterpriseJobMapper extends SuperMapper<EnterpriseJob> {

    /**
     * 自定义查询
     *
     * @param params
     * @return
     */
    List<EnterpriseJob> selectEnterpriseJobList(@Param("ew") Map<String, Object> params);
}
