package com.example.demo.service.impl;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.RedRecords;
import com.example.demo.entity.Reds;
import com.example.demo.mapper.RedRecordsMapper;
import com.example.demo.service.RedPackageService;
import com.example.demo.service.RedsService;
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

    @Override
    @Transactional
    public String snatchPackage(UserDto dto, int redId) throws Exception {
        String key = "RedPackage|" + redId;
        Reds red = redsservice.getRedsById(redId);
        Object obj = redisTemplate.opsForValue().get(key);
        List<RedRecords> list = redrecordsmapper.getRecord(redId, dto.getId());
        if (!CollectionUtils.isEmpty(list)) {
            throw new Exception("您已打开过该红包，只能打开一次！");
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
                return String.valueOf(costb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();//解锁
        }
        return null;
    }
}
