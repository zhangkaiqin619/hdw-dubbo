package com.hdw.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.hdw.common.mybatis.base.service.impl.BaseServiceImpl;
import com.hdw.common.mybatis.base.vo.PageVo;
import com.hdw.sms.entity.SmsRecord;
import com.hdw.sms.mapper.SmsRecordMapper;
import com.hdw.sms.dto.SmsRecordDTO;
import com.hdw.sms.service.ISmsRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 消息记录表
 *
 * @Author TuMinglong
 * @Date 2019-07-31 16:31:12
 */
@Slf4j
@Service(interfaceName = "ISmsRecordService")
@Transactional(rollbackFor = Exception.class)
public class SmsRecordServiceImpl extends BaseServiceImpl<SmsRecordMapper, SmsRecord> implements ISmsRecordService {

    public PageVo selectSmsRecordPageList(SmsRecordDTO smsRecordDTO) {
        QueryWrapper<SmsRecord> wrapper = new QueryWrapper(smsRecordDTO);
        wrapper.like(ObjectUtils.isNotEmpty(smsRecordDTO.getUserName()), "t3.name", smsRecordDTO.getUserName())
                .ge(ObjectUtils.isNotEmpty(smsRecordDTO.getStartTime()), "t.push_time", smsRecordDTO.getStartTime())
                .le(ObjectUtils.isNotEmpty(smsRecordDTO.getEndTime()), "t.push_time", smsRecordDTO.getEndTime())
                .eq(ObjectUtils.isNotEmpty(smsRecordDTO.getUserId()), "t.user_id", smsRecordDTO.getUserId());
        if (ObjectUtils.isNotEmpty(smsRecordDTO.getStatus())){
            if(smsRecordDTO.getStatus() == "-1"){
                wrapper.and(i -> i.ne("t.status", "0").ne("t.status", "3"));
            }else{
                wrapper.eq(ObjectUtils.isNotEmpty(smsRecordDTO.getStatus()), "t.status", smsRecordDTO.getStatus());
            }
        }
        wrapper.orderByDesc("t.push_time");
        Page page = new Page();
        // 设置当前页码
        page.setCurrent(smsRecordDTO.getPage());
        // 设置页大小
        page.setSize(smsRecordDTO.getLimit());
        IPage ipage = this.baseMapper.selectSmsRecordPageList(page, wrapper);
        return new PageVo(ipage);
    }


    @Override
    public Integer findUnreadMessagesCount(Long userId) {
        QueryWrapper<SmsRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.and(i -> i.ne("status", "0").ne("status", "3"));
        wrapper.orderByDesc("push_time");
        return this.count(wrapper);
    }

    @Override
    public List<SmsRecord> findRecent5Messages(Long userId) {
        return this.baseMapper.findRecent5Messages(userId);
    }

    @Override
    public void updateMessageStatus(String[] ids) {
        List<SmsRecord> list = Lists.newArrayList();
        for (String s : ids) {
            SmsRecord smsRecord = new SmsRecord();
            smsRecord.setId(Long.valueOf(s));
            smsRecord.setStatus(3);
            list.add(smsRecord);
        }
        this.updateBatchById(list);
    }
}
