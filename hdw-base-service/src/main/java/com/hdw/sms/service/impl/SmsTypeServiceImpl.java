package com.hdw.sms.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hdw.common.mybatis.base.service.impl.BaseServiceImpl;
import com.hdw.common.mybatis.base.vo.PageVo;
import com.hdw.sms.entity.SmsType;
import com.hdw.sms.mapper.SmsTypeMapper;
import com.hdw.sms.dto.SmsTypeDTO;
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
@Service(interfaceName = "ISmsTypeService")
@Transactional(rollbackFor = Exception.class)
public class SmsTypeServiceImpl extends BaseServiceImpl<SmsTypeMapper, SmsType> implements ISmsTypeService {

    public PageVo selectSmsTypePageList(SmsTypeDTO smsTypeDTO) {
        QueryWrapper<SmsType> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .like(ObjectUtils.isNotEmpty(smsTypeDTO.getTypeName()), SmsType::getTypeName, smsTypeDTO.getTypeName());
        queryWrapper.orderByDesc("create_time");
        Page page = new Page();
        // 设置当前页码
        page.setCurrent(smsTypeDTO.getPage());
        // 设置页大小
        page.setSize(smsTypeDTO.getLimit());
        IPage ipage = this.baseMapper.selectPage(page, queryWrapper);
        return new PageVo(ipage);
    }
}
