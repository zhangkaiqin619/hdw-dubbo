package com.hdw.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Description Spring Context 工具类
 * @Author TuMinglong
 * @Date 2018/12/10 11:56
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {
    public static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }


    public static <T> T getBean(Class<T> clazz) {

        return (T) applicationContext.getBean(clazz);
    }

    public static boolean containsBean(String name) {

        return applicationContext.containsBean(name);
    }

    public static boolean isSingleton(String name) {

        return applicationContext.isSingleton(name);
    }

    public static Class<? extends Object> getType(String name) {

        return applicationContext.getType(name);
    }

}