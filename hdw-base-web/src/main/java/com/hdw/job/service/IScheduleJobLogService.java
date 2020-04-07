package com.hdw.job.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hdw.common.mybatis.base.vo.PageVo;
import com.hdw.job.entity.ScheduleJobLogEntity;
import com.hdw.job.dto.JobLogDTO;


/**
 * @Description 定时任务日志
 * @Author TuMinglong
 * @Date 2019/1/18 15:59
 **/
public interface IScheduleJobLogService extends IService<ScheduleJobLogEntity> {

    PageVo queryPage(JobLogDTO jobLogDTO);

}
