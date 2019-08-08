package com.hdw.sms.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdw.common.result.PageParams;
import com.hdw.sms.entity.SmsType;
import com.hdw.sms.mapper.SmsTypeMapper;
import com.hdw.sms.service.ISmsTypeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description com.hdw.sms.service.impl
 * @Author TuMinglong
 * @Date 2019/7/31 17:05
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SmsTypeServiceImpl extends ServiceImpl<SmsTypeMapper, SmsType> implements ISmsTypeService {


    @Override
    public PageParams selectDataGrid(PageParams pageParams) {
        SmsType query = pageParams.mapToObject(SmsType.class);
        QueryWrapper<SmsType> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .like(ObjectUtils.isNotEmpty(query.getTypeName()), SmsType::getTypeName, query.getTypeName());
        queryWrapper.orderByDesc("create_time");
        IPage ipage = this.baseMapper.selectPage(pageParams, queryWrapper);
        return new PageParams(ipage);
    }
}
