package com.hdw.job.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hdw.common.mybatis.base.vo.PageVo;
import com.hdw.job.entity.ScheduleJobEntity;
import com.hdw.job.dto.JobDTO;

/**
 * @Description 定时任务
 * @Author TuMinglong
 * @Date 2019/1/18 15:59
 **/
public interface IScheduleJobService extends IService<ScheduleJobEntity> {

    PageVo queryPage(JobDTO jobDTO);

    /**
     * 保存定时任务
     */
    void insert(ScheduleJobEntity scheduleJob);

    /**
     * 更新定时任务
     */
    void update(ScheduleJobEntity scheduleJob);

    /**
     * 批量删除定时任务
     */
    void deleteBatch(Long[] jobIds);

    /**
     * 批量更新定时任务状态
     */
    int updateBatch(Long[] jobIds, int status);

    /**
     * 立即执行
     */
    void run(Long[] jobIds);

    /**
     * 暂停运行
     */
    void pause(Long[] jobIds);

    /**
     * 恢复运行
     */
    void resume(Long[] jobIds);
}
