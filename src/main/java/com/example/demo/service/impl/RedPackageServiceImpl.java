package com.example.demo.service.impl;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.RedKey;
import com.example.demo.entity.RedRecords;
import com.example.demo.entity.Reds;
import com.example.demo.entity.User;
import com.example.demo.mapper.RedRecordsMapper;
import com.example.demo.service.RedKeyService;
import com.example.demo.service.RedPackageService;
import com.example.demo.service.RedsService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class RedPackageServiceImpl implements RedPackageService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private RedRecordsMapper redrecordsmapper;
    @Resource
    private RedsService redsservice;
    @Resource
    private UserService userservice;
    @Resource
    private RedKeyService redkeyservice;

    @Override
    @Transactional
    public String snatchPackage(UserDto dto, int redId) throws Exception {
        String key = "RedPackage|" + redId;
        Reds red = redsservice.getRedsById(redId);
        Object obj = redisTemplate.opsForValue().get(key);
        List<RedRecords> list = redrecordsmapper.getRecord(redId, dto.getId(), 1);
        if (!CollectionUtils.isEmpty(list)) {
            throw new Exception("您已打开过该红包，只能打开一次！");
        }
        if (red.getNum() < 1) {
            throw new Exception("您来晚了，红包抢完了！");
        }
        Lock lock = new ReentrantLock();// 重入锁
        try {
            lock.lock();//加锁
            if (!ObjectUtils.isEmpty(obj)) {
                int num = (int) obj;//库存
                red.setNum(num - 1);
                Random rd = new Random();
                double end = red.getPercentage() != null ? red.getPercentage().doubleValue() : 0;
                double begin = red.getPercentageMin() != null ? red.getPercentageMin().doubleValue() : 0;
                double cost = rd.nextDouble() * (end - begin) + begin;
                BigDecimal costb = BigDecimal.valueOf(cost).setScale(2, BigDecimal.ROUND_HALF_UP);
                //减库存
                redsservice.updateNumbReds(red);
                RedRecords redRecords = RedRecords.builder()
                        .redId(redId)
                        .userId(dto.getId())
                        .bean(costb)
                        .type(1)
                        .build();
                //保存记录
                redrecordsmapper.saveRecords(redRecords);
                //
                BigDecimal balance = dto.getBean().add(costb);
                //金币
                userservice.updateBean(balance, dto.getId());
                return String.valueOf(costb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();//解锁
        }
        return null;
    }

    @Override
    @Transactional
    public String snatchKeyPackage(UserDto dto, String keyCode) throws Exception {
        User us = userservice.getUserById(dto.getId());
        RedKey redkey = redkeyservice.getRedsByKey(keyCode);
        if (ObjectUtils.isEmpty(redkey)) {
            throw new Exception("红包已经领光了");
        }
        List<RedRecords> list = redrecordsmapper.getRecord(redkey.getId(), dto.getId(), 2);
        if (!CollectionUtils.isEmpty(list)) {
            throw new Exception("您已打开过该红包，只能打开一次！");
        }
        redkeyservice.updateStatus(keyCode);
        RedRecords redRecords = RedRecords.builder()
                .redId(redkey.getId())
                .userId(dto.getId())
                .bean(redkey.getBean())
                .type(2)
                .build();
        //保存记录
        redrecordsmapper.saveRecords(redRecords);
        BigDecimal balance = us.getBean().add(redkey.getBean());
        //得到金币
        userservice.updateBean(balance, dto.getId());
        return String.valueOf(redkey.getBean());
    }

}
