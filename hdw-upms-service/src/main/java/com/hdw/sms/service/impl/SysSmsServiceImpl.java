package com.hdw.sms.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdw.common.result.PageParams;
import com.hdw.sms.entity.SysSms;
import com.hdw.sms.mapper.SysSmsMapper;
import com.hdw.sms.service.ISysSmsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 消息表
 *
 * @Author TuMinglong
 * @Date 2019-07-31 16:31:12
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysSmsServiceImpl extends ServiceImpl<SysSmsMapper, SysSms> implements ISysSmsService {


    @Override
    public PageParams selectDataGrid(PageParams pageParams) {
        SysSms query = pageParams.mapToObject(SysSms.class);
        QueryWrapper<SysSms> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(ObjectUtils.isNotEmpty(query.getTitle()), SysSms::getTitle, query.getTitle())
                .eq(ObjectUtils.isNotEmpty(query.getSmsTypeId()), SysSms::getSmsTypeId, query.getSmsTypeId());
        queryWrapper.like(ObjectUtils.isNotEmpty(query.getTypeName()), "t2.type_name", query.getTypeName());
        queryWrapper.orderByDesc("create_time");
        IPage ipage = this.baseMapper.selectPageList(pageParams, queryWrapper);
        return new PageParams(ipage);
    }

    @Override
    public List<SysSms> selectCurrentSmsList() {
        return this.baseMapper.selectCurrentSmsList();
    }
}
