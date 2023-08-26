package com.example.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.demo.entity.RechangeWelfare;
import com.example.demo.entity.User;
import com.example.demo.entity.WelfareRedis;
import com.example.demo.service.UserService;
import com.example.demo.service.WelfareService;
import com.example.demo.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class WelfareServiceImpl implements WelfareService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private UserService userservice;

    @Override
    public List<RechangeWelfare> getRechargeWelfare(User usr, int type) {
        if (type == 1) {
            String userKey = "UserRecharge|Day" + usr.getId();
            return this.getListLucky("/welfare/welfare_day.json", userKey);
        } else if (type == 2) {
            String userKey = "UserRecharge|Month" + usr.getId();
            return this.getListLucky("/welfare/welfare_month.json", userKey);
        } else if (type == 3) {
            String userKey = "UserBlindBox|Day" + usr.getId();
            return this.getListLucky("/welfare/welfare_mh.json", userKey);
        }
        return null;
    }

    private List<RechangeWelfare> getListLucky(String file, String key) {
        String jstr = this.getJsonForFile(file);
        List<RechangeWelfare> listLucky = JSON.parseArray(jstr, RechangeWelfare.class);
        listLucky.stream().forEach(e -> {
            //获取当日充值
            Object str = redisTemplate.opsForValue().get(key);
            if (!ObjectUtils.isEmpty(str)) {
                WelfareRedis red = JSON.parseObject(str.toString(), WelfareRedis.class);
                e.setMycost(red.getCost());
                if (red.getCost() >= e.getCost()) {
                    e.setStatus("2");
                }
                if (!CollectionUtils.isEmpty(red.getList())) {
                    red.getList().stream().forEach(re -> {
                        if (re == e.getId()) {
                            e.setStatus("1");
                        }
                    });
                }
            }
        });
        return listLucky;
    }

    @Override
    public int getWelfare(User usr, int id, int type) {
        int rewardCount = 0;
        if (type == 1) {
            String userKey = "UserRecharge|Day" + usr.getId();
            rewardCount = this.getReawardById(usr, id, "/welfare/welfare_day.json", userKey);
        } else if (type == 2) {
            String userKey = "UserRecharge|Month" + usr.getId();
            rewardCount = this.getReawardById(usr, id, "/welfare/welfare_month.json", userKey);
        } else if (type == 3) {
            String userKey = "UserRecharge|MH" + usr.getId();
            rewardCount = this.getReawardById(usr, id, "/welfare/welfare_mh.json", userKey);
        }
        return rewardCount;
    }

    @Override
    public int getDays(User usr) {
        Object str = redisTemplate.opsForValue().get("UserWelfare|Day" + usr.getId());
        if (!ObjectUtils.isEmpty(str)) {
            return Integer.parseInt(str.toString());
        }
        return 0;
    }


    private int getReawardById(User usr, int id, String file, String userKey) {
        String jstr = this.getJsonForFile(file);
        List<RechangeWelfare> listLucky = JSON.parseArray(jstr, RechangeWelfare.class);
        for (RechangeWelfare e : listLucky) {
            if (e.getId() == id) {
                String[] reward = e.getReward().split("-");
                Random random = new Random();
                int number = random.nextInt(Integer.parseInt(reward[1]) - Integer.parseInt(reward[0]) + 1) + Integer.parseInt(reward[0]);
                Object str = redisTemplate.opsForValue().get(userKey);
                if (!ObjectUtils.isEmpty(str)) {
                    WelfareRedis red = JSON.parseObject(str.toString(), WelfareRedis.class);
                    //判断是否有领取
                    boolean bl = false;
                    for (Integer integer : red.getList()) {
                        if (integer == id) {
                            bl = true;
                        }
                    }
                    if (!bl) {//么有领取过
                        BigDecimal balance = usr.getBean().add(new BigDecimal(number));
                        userservice.updateBean(balance, usr.getId());
                        List<Integer> list = red.getList();
                        list.add(id);
                        red.setList(list);
                        redisTemplate.opsForValue().set(userKey, JSON.toJSON(red));
                        Object ostr = redisTemplate.opsForValue().get("UserWelfare|Day" + usr.getId());
                        int count = 0;
                        long time = DateUtils.getTime();
                        if (!ObjectUtils.isEmpty(ostr)) {
                            count = Integer.parseInt(ostr.toString());
                        }
                        redisTemplate.opsForValue().set("UserWelfare|Day" + usr.getId(), count + number, time, TimeUnit.SECONDS);
                        return number;
                    } else {
                        log.error("已经领取。重复领取====");
                        return 0;
                    }
                }
            }
        }
        return 0;
    }

    private String getJsonForFile(String str) {
        String jsonStr = null;
        try {
            InputStream inputStream = this.getClass().getResourceAsStream(str);
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            int ch = 0;
            StringBuilder sb = new StringBuilder();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            reader.close();
            jsonStr = sb.toString();
            System.out.println(jsonStr);
            return jsonStr;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
