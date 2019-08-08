package com.hdw.sms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.hdw.common.result.PageParams;
import com.hdw.sms.entity.SmsRecord;
import com.hdw.sms.mapper.SmsRecordMapper;
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
@Service
@Transactional(rollbackFor = Exception.class)
public class SmsRecordServiceImpl extends ServiceImpl<SmsRecordMapper, SmsRecord> implements ISmsRecordService {


    @Override
    public PageParams selectDataGrid(PageParams pageParams) {
        SmsRecord query = pageParams.mapToObject(SmsRecord.class);
        QueryWrapper<SmsRecord> wrapper = new QueryWrapper(pageParams);
        wrapper.like(ObjectUtils.isNotEmpty(query.getUserName()), "t3.name", query.getUserName())
                .ge(ObjectUtils.isNotEmpty(query.getStartTime()), "t.push_time", query.getStartTime())
                .le(ObjectUtils.isNotEmpty(query.getEndTime()), "t.push_time", query.getEndTime())
                .eq(ObjectUtils.isNotEmpty(query.getUserId()), "t.user_id", query.getUserId());
        if (ObjectUtils.isNotEmpty(query.getStatus()) && query.getStatus() == -1) {
            wrapper.and(i -> i.ne("t.status", "0").ne("t.status", "3"));
        }
        wrapper.orderByDesc("t.push_time");
        IPage ipage = this.baseMapper.selectPageList(pageParams, wrapper);
        return new PageParams(ipage);
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
