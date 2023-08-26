package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.example.demo.entity.WelfareRedis;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WelfareDayTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void dayPay() {
        //计算失效时间
        try {
            Date date1 = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.setTime(date1);
            c.add(Calendar.DAY_OF_MONTH, 1);
            String date = sdf.format(c.getTime());
            Date date2 = DateUtils.parseDate(date + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(date2);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(new Date());
            long time = calendar1.getTimeInMillis() / 1000 - calendar2.getTimeInMillis() / 1000;
            String userKey = "UserRecharge|Day" + 917;
            Object str = redisTemplate.opsForValue().get(userKey);
            WelfareRedis red = null;
            if (!ObjectUtils.isEmpty(str)) {
                red = JSON.parseObject(str.toString(), WelfareRedis.class);
                red.setCost(red.getCost() + 50);
            } else {
                red = new WelfareRedis();
                red.setCost(50);
                red.setUserId(917);
                red.setList(new ArrayList<>());
            }
            redisTemplate.opsForValue().set(userKey, JSON.toJSON(red), time, TimeUnit.SECONDS);
            System.out.println(JSON.toJSON(red));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Test
    void monthPay() {
        //计算失效时间
        try {
            // 获取当前月份最后一天
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            SimpleDateFormat lastDayFormat = new SimpleDateFormat("yyyy-MM-dd");
            String lastDay = lastDayFormat.format(calendar.getTime());
            Date date2 = DateUtils.parseDate(lastDay + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(date2);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(new Date());
            long time = calendar1.getTimeInMillis() / 1000 - calendar2.getTimeInMillis() / 1000;
            String userKey = "UserRecharge|Month" + 917;
            Object str = redisTemplate.opsForValue().get(userKey);
            WelfareRedis red = null;
            if (!ObjectUtils.isEmpty(str)) {
                red = JSON.parseObject(str.toString(), WelfareRedis.class);
                red.setCost(red.getCost() + 50);
            } else {
                red = new WelfareRedis();
                red.setCost(50);
                red.setUserId(917);
                red.setList(new ArrayList<>());
            }
            redisTemplate.opsForValue().set(userKey, JSON.toJSON(red), time, TimeUnit.SECONDS);
            System.out.println(JSON.toJSON(red));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
