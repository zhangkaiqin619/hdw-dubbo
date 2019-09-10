package com.hdw.common.config.redis;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Description redis工具接口实现类
 * @Author TuMinglong
 * @Date 2019/9/4 11:57
 */
@Component
public class RedisServiceImpl implements IRedisService {

    @Resource(name = "jsonRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${hdw.expire}")
    private Integer EXPIRE;

    @Override
    public Boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    @Override
    public Boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean del(String... key) {
        try {
            if (key != null && key.length > 0) {
                if (key.length == 0) {
                    redisTemplate.delete(key[0]);
                } else {
                    redisTemplate.delete(CollectionUtils.arrayToList(key));
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public <T> T get(String key) {
        return key == null ? null : (T) redisTemplate.boundValueOps(key).get();
    }

    @Override
    public String getString(String key) {
        String str = "";
        Object obj = redisTemplate.boundValueOps(key).get();
        if (ObjectUtils.isNotNull(obj)) {
            str = obj.toString();
        }
        return key == null ? null : str;
    }

    @Override
    public <T> Boolean set(String key, T value) {
        try {
            redisTemplate.boundValueOps(key).set(value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public <T> Boolean set(String key, T value, long time) {
        try {
            if (time > 0) {
                redisTemplate.boundValueOps(key).set(value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    @Override
    public Long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    @Override
    public Object hGet(String key, String item) {
        return redisTemplate.boundHashOps(key).get(item);
    }

    @Override
    public Map<Object, Object> getMap(String key) {
        return redisTemplate.boundHashOps(key).entries();
    }

    @Override
    public List<Object> hValues(String key) {
        return redisTemplate.boundHashOps(key).values();
    }

    @Override
    public Long hSize(String key) {
        return redisTemplate.boundHashOps(key).size();
    }

    @Override
    public <T> Boolean setMap(String key, String hashKey, T value) {
        try {
            redisTemplate.boundHashOps(key).put(hashKey, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public <T> Boolean setMap(String key, String hashKey, T value, long time) {
        try {
            redisTemplate.boundHashOps(key).put(hashKey, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean setMap(String key, Map<String, Object> map) {
        try {
            redisTemplate.boundHashOps(key).putAll(map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean setMap(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.boundHashOps(key).putAll(map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Long hDel(String key, Object... hashKeys) {

        return redisTemplate.boundHashOps(key).delete(hashKeys);
    }

    @Override
    public Boolean hHasKey(String key, Object hashKey) {
        return redisTemplate.boundHashOps(key).hasKey(hashKey);
    }

    @Override
    public Double hIncr(String key, String hashKey, double delta) {
        return redisTemplate.boundHashOps(key).increment(hashKey, delta);
    }

    @Override
    public Double hDecr(String key, String hashKey, double delta) {
        return redisTemplate.boundHashOps(key).increment(hashKey, -delta);
    }

    @Override
    public Long hIncr(String key, String hashKey, long delta) {
        return redisTemplate.boundHashOps(key).increment(hashKey, delta);
    }

    @Override
    public Long hDecr(String key, String hashKey, long delta) {
        return redisTemplate.boundHashOps(key).increment(hashKey, -delta);
    }

    @Override
    public Set<Object> getSet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    @Override
    public <T> Boolean sHasKey(String key, T value) {
        return redisTemplate.boundSetOps(key).isMember(value);
    }

    @Override
    public <T> Long sSet(String key, T... values) {
        try {
            return redisTemplate.boundSetOps(key).add(values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Override
    public <T> Long sSet(String key, long time, T... values) {
        try {
            Long count = redisTemplate.boundSetOps(key).add(values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Override
    public Long sSize(String key) {
        try {
            return redisTemplate.boundSetOps(key).size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Override
    public Long sDel(String key, Object... values) {
        try {
            Long count = redisTemplate.boundSetOps(key).remove(values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Override
    public List<Object> getList(String key, long start, long end) {
        try {
            return redisTemplate.boundListOps(key).range(start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Object> getList(String key) {
        try {
            return redisTemplate.boundListOps(key).range(0, -1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Long lSize(String key) {
        try {
            return redisTemplate.boundListOps(key).size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Override
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.boundListOps(key).index(index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public <T> Boolean setList(String key, T value) {
        try {
            redisTemplate.boundListOps(key).rightPush(value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public <T> Boolean setList(String key, T value, long time) {
        try {
            redisTemplate.boundListOps(key).rightPush(value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public <T> Boolean setList(String key, List<T> value) {
        try {
            redisTemplate.boundListOps(key).rightPushAll(value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public <T> Boolean setList(String key, List<T> value, long time) {
        try {
            redisTemplate.boundListOps(key).rightPushAll(value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public <T> Boolean lUpdateIndex(String key, long index, T value) {
        try {
            redisTemplate.boundListOps(key).set(index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public <T> Long lDel(String key, long count, T value) {
        try {
            Long remove = redisTemplate.boundListOps(key).remove(count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Override
    public void lTrim(String key, long start, long end) {
        redisTemplate.boundListOps(key).trim(start, end);
    }
}
