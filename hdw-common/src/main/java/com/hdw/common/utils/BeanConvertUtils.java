package com.hdw.common.utils;


import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description 实体工具类
 * @Author TuMinglong
 * @Date 2019/5/10 9:43
 **/
public class BeanConvertUtils {

    /**
     * 实例化对象
     *
     * @param clazz 类
     * @param <T>   对象
     * @return
     */
    public static <T> T newInstance(Class<?> clazz) {
        return (T) BeanUtils.instantiateClass(clazz);
    }

    /**
     * 实例化对象
     *
     * @param clazzStr 类名
     * @param <T>      对象
     * @return
     */
    public static <T> T newInstance(String clazzStr) {
        try {
            Class<?> clazz = Class.forName(clazzStr);
            return newInstance(clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置Bean对象的名称为name的property的值为value
     *
     * @param bean
     * @param name
     * @param value
     */
    public static void setProperty(Object bean, String name, Object value) {
        try {
            org.apache.commons.beanutils.BeanUtils.setProperty(bean, name, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取bean对象中名为name的属性的值。
     *
     * @param bean
     * @param name
     * @return
     */
    public static String getProperty(Object bean, String name) {
        if (null == bean || StringUtils.isEmpty(name)) {
            return "";
        }
        try {
            return org.apache.commons.beanutils.BeanUtils.getProperty(bean, name);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 将bean转化为另一种bean实体
     *
     * @param object
     * @param entityClass
     * @param <T>
     * @return
     */
    public static <T> T convertBean(Object object, Class<T> entityClass) {
        if (null == object) {
            return null;
        }
        return JSON.parseObject(JSON.toJSONString(object), entityClass);
    }

    /**
     * 对象转换
     *
     * @param source 源对象
     * @param target 目标对象
     * @param <T>
     * @return
     */
    public static <T> T copy(Object source, Class<T> target) {
        T targetInstance = null;
        try {
            targetInstance = target.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        BeanUtils.copyProperties(source, targetInstance);
        return targetInstance;
    }

    /**
     * 对象转换
     *
     * @param source           源对象
     * @param target           目标对象
     * @param ignoreProperties 排除要复制的属性
     * @param <T>
     * @return
     */
    public static <T> T copy(Object source, Class<T> target, String... ignoreProperties) {
        T targetInstance = null;
        try {
            targetInstance = target.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (ArrayUtils.isEmpty(ignoreProperties)) {
            BeanUtils.copyProperties(source, targetInstance);
        } else {
            BeanUtils.copyProperties(source, targetInstance, ignoreProperties);
        }
        return targetInstance;
    }

    /**
     * 对象装换（List）
     *
     * @param list             源对象
     * @param target           目标对象
     * @param ignoreProperties 要排除复制的属性
     * @param <T>
     * @param <E>
     * @return
     */
    public static <T, E> List<T> copyList(List<E> list, Class<T> target, String... ignoreProperties) {
        List<T> targetList = new ArrayList<T>();
        if (CollectionUtils.isEmpty(list)) {
            return targetList;
        }
        for (E e : list) {
            targetList.add(copy(e, target, ignoreProperties));
        }
        return targetList;
    }

    /**
     * 对象装换（List）
     *
     * @param list   源对象
     * @param target 目标对象
     * @param <T>
     * @param <E>
     * @return
     */
    public static <T, E> List<T> copyList(List<E> list, Class<T> target) {
        List<T> targetList = new ArrayList<T>();
        if (CollectionUtils.isEmpty(list)) {
            return targetList;
        }
        for (E e : list) {
            targetList.add(copy(e, target));
        }
        return targetList;
    }

    /**
     * map转化为对象
     *
     * @param map   源map
     * @param t 目标对象
     * @param <T>
     * @return
     */
    public static <T> T mapToObject(Map<String, Object> map, Class<T> t) {
        try {
            T instance = t.newInstance();
            org.apache.commons.beanutils.BeanUtils.populate(instance, map);
            return instance;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 对象转map
     *
     * @param object
     * @return
     */
    public static Map<?, ?> objectToMap(Object object) {
        return convertBean(object, Map.class);
    }
}
