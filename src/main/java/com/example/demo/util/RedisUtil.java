package com.example.demo.util;

import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisUtil {
    /**
     * RedisTemplate不能作为静态变量注入, 否则报空指针.
     */
    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     */
    public void expire(String key, long time) {
        if (time > 0 && !StringUtil.isNullOrEmpty(key)) {
            try {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public Long getExpire(String key) {
        return !StringUtil.isNullOrEmpty(key) ? redisTemplate.getExpire(key, TimeUnit.SECONDS) : null;
    }


    /**
     * 判断key是否存在
     * * @param key 键
     *
     * @return true 存在 false不存在
     */
    public Boolean hasKey(String key) {
        Boolean flag = false;
        if (!StringUtil.isNullOrEmpty(key)) {
            try {
                flag = redisTemplate.hasKey(key);
            } catch (Exception e) {
                log.error("redis中不包含" + key);
            }
        }
        return flag;
    }


    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }


    //============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return StringUtil.isNullOrEmpty(key) ? null : redisTemplate.opsForValue().get(key);
    }


    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        if (!StringUtil.isNullOrEmpty(key)) {
            try {
                redisTemplate.opsForValue().set(key, value);
                return true;
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        return false;
    }


    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        if (!StringUtil.isNullOrEmpty(key)) {
            if (time > 0) {
                try {
                    redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                return set(key, value);
            }
        }
        return false;
    }



    //================================Map=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

}
