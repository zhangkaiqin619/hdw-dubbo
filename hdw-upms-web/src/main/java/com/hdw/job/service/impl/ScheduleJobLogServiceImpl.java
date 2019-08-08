package com.hdw.job.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdw.common.result.PageParams;
import com.hdw.job.entity.ScheduleJobLogEntity;
import com.hdw.job.mapper.ScheduleJobLogMapper;
import com.hdw.job.service.IScheduleJobLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("scheduleJobLogService")
public class ScheduleJobLogServiceImpl extends ServiceImpl<ScheduleJobLogMapper, ScheduleJobLogEntity> implements IScheduleJobLogService {

    @Override
    public PageParams queryPage(Map<String, Object> params) {
        String jobId = (String) params.get("jobId");
        PageParams pageParams = new PageParams(params);
        QueryWrapper<ScheduleJobLogEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(jobId), "job_id", jobId)
                .orderByDesc("log_id");
        IPage<ScheduleJobLogEntity> iPage = this.page(pageParams, queryWrapper);
        return new PageParams(iPage);
    }

}
