package com.hdw.task;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;

/**
 * @Description Spring定时任务线程池配置
 * @Author TuMingLong
 * @Date 2020/4/1 23:45
 */
@Configuration
@EnableScheduling
public class ScheduledThreadPoolConfig implements SchedulingConfigurer {
    /**
     * 对于CPU密集型任务，最大线程数是CPU线程数+1。对于IO密集型任务，尽量多配点，可以是CPU线程数*2，或者CPU线程数/(1-阻塞系数)。
     * maxPoolSize=new Double(Math.floor(Runtime.getRuntime().availableProcessors()/(1-0.9))).intValue()
     *
     * @param taskRegistrar
     */

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors()));
    }
}
