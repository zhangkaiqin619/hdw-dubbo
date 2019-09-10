package com.hdw.common.config.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description redis工具接口
 * @Author TuMinglong
 * @Date 2019/9/4 11:18
 */
public interface IRedisService {

    /**
     * 指定缓存时间
     *
     * @param key
     * @param time
     * @return
     */
    Boolean expire(String key, long time);

    /**
     * 根据key 获取过期时间
     *
     * @param key
     * @return
     */
    Long getExpire(String key);

    /**
     * 判断key是否存在
     *
     * @param key
     * @return
     */
    Boolean hasKey(String key);

    /**
     * 删除缓存
     *
     * @param key 可以传一个值或多个
     * @return
     */
    Boolean del(String... key);

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @param <T> 值
     * @return
     */
    <T> T get(String key);

    /**
     * 普通缓存获取
     *
     * @param key
     * @return
     */
    String getString(String key);

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @param <T>
     * @return true成功 false失败
     */
    <T> Boolean set(String key, T value);

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间（秒） time要大于1如果time小于等于0将设置无期限
     * @param <T>
     * @return
     */
    <T> Boolean set(String key, T value, long time);

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几（大于0）
     * @return
     */
    Long incr(String key, long delta);

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几（小于0）
     * @return
     */
    Long decr(String key, long delta);

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return
     */
    Object hGet(String key, String item);

    /**
     * 键
     *
     * @param key
     * @return 对应的多个键值
     */
    Map<Object, Object> getMap(String key);

    /**
     * 获取map集合中所有元素的value
     *
     * @param key 键
     * @return
     */
    List<Object> hValues(String key);

    /**
     * 获取map集合长度
     *
     * @param key 键
     * @return
     */
    Long hSize(String key);

    /**
     * HashSet
     *
     * @param key     key
     * @param hashKey map中key值
     * @param value   map中value值
     * @return
     */
    <T> Boolean setMap(String key, String hashKey, T value);

    /**
     * HashSet
     *
     * @param key     key
     * @param hashKey map中key值
     * @param value   map中value值
     * @param time    时间（秒） time要大于1如果time小于等于0将设置无期限
     * @return
     */
    <T> Boolean setMap(String key, String hashKey, T value, long time);

    /**
     * HashSet
     *
     * @param key 键
     * @param map map集合
     * @return
     */
    Boolean setMap(String key, Map<String, Object> map);

    /**
     * HashSet
     *
     * @param key  键
     * @param map  map集合
     * @param time 时间（秒） time要大于1如果time小于等于0将设置无期限
     * @return
     */
    Boolean setMap(String key, Map<String, Object> map, long time);

    /**
     * 删除map集合中一个或多个hashKey对应的value
     *
     * @param key      键
     * @param hashKeys 可以传一个值或多个
     * @return
     */
    Long hDel(String key, Object... hashKeys);

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key     键
     * @param hashKey 项
     * @return
     */
    Boolean hHasKey(String key, Object hashKey);

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key     键
     * @param hashKey map中key值
     * @param delta   要增加几（大于）
     * @return
     */
    Double hIncr(String key, String hashKey, double delta);

    /**
     * hash递减
     *
     * @param key     键
     * @param hashKey map中key
     * @param delta   要减少几（小于）
     * @return
     */
    Double hDecr(String key, String hashKey, double delta);

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key     键
     * @param hashKey map中key值
     * @param delta   要增加几（大于）
     * @return
     */
    Long hIncr(String key, String hashKey, long delta);

    /**
     * hash递减
     *
     * @param key     键
     * @param hashKey map中key
     * @param delta   要减少几（小于）
     * @return
     */
    Long hDecr(String key, String hashKey, long delta);

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    Set<Object> getSet(String key);

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @param <T>
     * @return
     */
    <T> Boolean sHasKey(String key, T value);

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以多个
     * @param <T>
     * @return 成功个数
     */
    <T> Long sSet(String key, T... values);

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param time   时间（秒）
     * @param values 值 可以多个
     * @param <T>
     * @return 成功个数
     */
    <T> Long sSet(String key, long time, T... values);

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    Long sSize(String key);

    /**
     * 移除值为value
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return
     */
    Long sDel(String key, Object... values);

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1 代表所有值
     * @return
     */
    List<Object> getList(String key, long start, long end);

    /**
     * 获取list缓存的内容
     *
     * @param key 键
     * @return
     */
    List<Object> getList(String key);

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    Long lSize(String key);

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 首个元素，依次类推；index<0时，-1，最后一个元素，-2倒数第二个元素，依次类推
     * @return
     */
    Object lGetIndex(String key, long index);

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    <T> Boolean setList(String key, T value);

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    <T> Boolean setList(String key, T value, long time);

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    <T> Boolean setList(String key, List<T> value);

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    <T> Boolean setList(String key, List<T> value, long time);

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    <T> Boolean lUpdateIndex(String key, long index, T value);

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    <T> Long lDel(String key, long count, T value);

    /**
     * 修剪集合中指定范围的元素
     *
     * @param key   键
     * @param start 起始位置
     * @param end   结束为止
     */
    void lTrim(String key, long start, long end);
}
