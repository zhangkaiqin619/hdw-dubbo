package com.hdw.sms.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hdw.common.mybatis.base.service.impl.BaseServiceImpl;
import com.hdw.common.mybatis.base.vo.PageVo;
import com.hdw.sms.entity.SysSms;
import com.hdw.sms.mapper.SysSmsMapper;
import com.hdw.sms.dto.SmsDTO;
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
@Service(interfaceName = "ISysSmsService")
@Transactional(rollbackFor = Exception.class)
public class SysSmsServiceImpl extends BaseServiceImpl<SysSmsMapper, SysSms> implements ISysSmsService {

    public PageVo selectSmsPageList(SmsDTO smsDTO) {
        QueryWrapper<SysSms> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(ObjectUtils.isNotEmpty(smsDTO.getTitle()), SysSms::getTitle, smsDTO.getTitle())
                .eq(ObjectUtils.isNotEmpty(smsDTO.getSmsTypeId()), SysSms::getSmsTypeId, smsDTO.getSmsTypeId());
        queryWrapper.like(ObjectUtils.isNotEmpty(smsDTO.getTypeName()), "t2.type_name", smsDTO.getTypeName());
        queryWrapper.orderByDesc("create_time");
        Page page = new Page();
        // 设置当前页码
        page.setCurrent(smsDTO.getPage());
        // 设置页大小
        page.setSize(smsDTO.getLimit());
        IPage ipage = this.baseMapper.selectSmsPageList(page, queryWrapper);
        return new PageVo(ipage);
    }

    @Override
    public List<SysSms> selectCurrentSmsList() {
        return this.baseMapper.selectCurrentSmsList();
    }
}
