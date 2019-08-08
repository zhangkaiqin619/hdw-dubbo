package com.hdw.enterprise.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hdw.common.result.PageParams;
import com.hdw.enterprise.entity.EnterpriseJob;

import java.util.List;
import java.util.Map;

/**
 * 企业职务配置表
 *
 * @author TuMinglong
 * @date 2018-12-11 11:36:02
 */
public interface IEnterpriseJobService extends IService<EnterpriseJob> {

    /**
     * 多表页面信息查询
     *
     * @param params
     * @return
     */
    PageParams selectDataGrid(Map<String, Object> params);

    /**
     * 自定义查询
     *
     * @param params
     * @return
     */
    List<EnterpriseJob> selectEnterpriseJobList(Map<String, Object> params);

}

