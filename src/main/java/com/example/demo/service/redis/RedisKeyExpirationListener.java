package com.example.demo.service.redis;

import com.example.demo.service.GameBattleService;
import com.example.demo.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * redis 失效监听
 */
@Service
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {


    private static Logger log = LoggerFactory.getLogger(RedisKeyExpirationListener.class);

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Autowired
    private RoomService roomservice;
    @Resource
    private GameBattleService gamebattleservice;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        //获取过期的key
        String expireKey = message.toString();
        log.info("失效+key is:" + expireKey);
        System.out.println(expireKey);
        log.info("=======================redisKey 监听=======================");
        //插入一条数据，以充当锁使用
        String oldlock = this.getAndSet(expireKey + ".lock", "1");
        if ("1".equals(oldlock)) {
            log.info("===========当前服务没有竞争到锁===========");
            return;
        }
        //业务处理:ROLL房间业务
        if (expireKey.startsWith("ROOM|")) {
            String room = expireKey.replace("ROOM|", "");
            roomservice.endRoom(Integer.parseInt(room));
        } else if (expireKey.startsWith("BATTLE|")) {
            String battleId = expireKey.replace("BATTLE|", "");
            gamebattleservice.endBattle(Integer.parseInt(battleId));
        }
    }

    public <T> T getAndSet(final String key, T value) {
        T oldValue = null;
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            oldValue = (T) operations.getAndSet(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return oldValue;
    }
}
