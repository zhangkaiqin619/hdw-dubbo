package com.hdw.job.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdw.common.constant.CommonEnum;
import com.hdw.common.mybatis.base.vo.PageVo;
import com.hdw.job.entity.ScheduleJobEntity;
import com.hdw.job.entity.ScheduleJobLogEntity;
import com.hdw.job.mapper.ScheduleJobMapper;
import com.hdw.job.dto.JobDTO;
import com.hdw.job.service.IScheduleJobService;
import com.hdw.job.util.ScheduleUtil;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Scheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service("scheduleJobService")
public class ScheduleJobServiceImpl extends ServiceImpl<ScheduleJobMapper, ScheduleJobEntity> implements IScheduleJobService {

    @Resource
    private Scheduler scheduler;

    @Override
    public PageVo queryPage(JobDTO jobDTO) {
        String beanName = jobDTO.getBeanName();
        QueryWrapper<ScheduleJobEntity> queryWrapper = new QueryWrapper<ScheduleJobEntity>();
        queryWrapper.like(StringUtils.isNotBlank(beanName), "job_id", beanName)
                .orderByDesc("job_id");
        Page page = new Page();
        // 设置当前页码
        page.setCurrent(jobDTO.getPage());
        // 设置页大小
        page.setSize(jobDTO.getLimit());
        IPage<ScheduleJobLogEntity> iPage = this.page(page, queryWrapper);
        return new PageVo(iPage);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(ScheduleJobEntity scheduleJob) {
        scheduleJob.setCreateTime(new Date());
        scheduleJob.setStatus(CommonEnum.NORMAL.getCode());
        this.save(scheduleJob);

        ScheduleUtil.createScheduleJob(scheduler, scheduleJob);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScheduleJobEntity scheduleJob) {
        ScheduleUtil.updateScheduleJob(scheduler, scheduleJob);

        this.updateById(scheduleJob);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] jobIds) {
        for (Long jobId : jobIds) {
            ScheduleUtil.deleteScheduleJob(scheduler, jobId);
        }

        //删除数据
        this.removeByIds(Arrays.asList(jobIds));
    }

    @Override
    public int updateBatch(Long[] jobIds, int status) {
        Map<String, Object> map = new HashMap<>();
        map.put("list", Arrays.asList(jobIds));
        map.put("status", status);
        return this.baseMapper.updateBatch(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(Long[] jobIds) {
        for (Long jobId : jobIds) {
            ScheduleUtil.run(scheduler, this.getById(jobId));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pause(Long[] jobIds) {
        for (Long jobId : jobIds) {
            ScheduleUtil.pauseJob(scheduler, jobId);
        }

        updateBatch(jobIds, CommonEnum.PAUSE.getCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resume(Long[] jobIds) {
        for (Long jobId : jobIds) {
            ScheduleUtil.resumeJob(scheduler, jobId);
        }

        updateBatch(jobIds, CommonEnum.NORMAL.getCode());
    }

}
