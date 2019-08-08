package com.hdw.enterprise.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdw.common.result.PageParams;
import com.hdw.enterprise.entity.EnterpriseJob;
import com.hdw.enterprise.mapper.EnterpriseJobMapper;
import com.hdw.enterprise.service.IEnterpriseJobService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 企业职务配置表
 *
 * @author TuMinglong
 * @date 2018-12-11 11:36:02
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class EnterpriseJobServiceImpl extends ServiceImpl<EnterpriseJobMapper, EnterpriseJob> implements IEnterpriseJobService {

    @Override
    public PageParams selectDataGrid(Map<String, Object> params) {
        PageParams pageParams = new PageParams(params);
        IPage<Map<String, Object>> iPage = this.baseMapper.selectEnterpriseJobPage(pageParams, pageParams.getRequestMap());
        return new PageParams(iPage);
    }

    @Override
    public List<EnterpriseJob> selectEnterpriseJobList(Map<String, Object> par) {

        return this.baseMapper.selectEnterpriseJobList(par);
    }
}
