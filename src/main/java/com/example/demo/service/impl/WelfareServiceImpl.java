package com.example.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.demo.dto.BasePage;
import com.example.demo.dto.VipDto;
import com.example.demo.dto.VipReturnDto;
import com.example.demo.entity.*;
import com.example.demo.service.BeanRecordService;
import com.example.demo.service.UserService;
import com.example.demo.service.VipService;
import com.example.demo.service.WelfareService;
import com.example.demo.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
import java.util.ArrayList;
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
    @Resource
    private VipService vipservice;
    @Resource
    private BeanRecordService beanrecordservice;

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

    @Override
    public VipReturnDto getVipList(User usr) {
        String userKey = "UserVip|" + usr.getId();
        VipReturnDto returnDto = new VipReturnDto();
        //查询累计充值
        BigDecimal bg = beanrecordservice.queryAllBeanRecords(usr.getId());
        returnDto.setCount(bg);
        int nextLevel = usr.getVipLevel() + 1;
        if (nextLevel > 11) {
            nextLevel = 11;
        }
        returnDto.setNextLevel(nextLevel);
        List<VipDto> listVip = new ArrayList<>();
        BasePage page = new BasePage();
        page.setPageNo(1);
        page.setPageSize(30);
        List<Vip> list = vipservice.getVipListByPage(page).getList();
        for (Vip e : list) {
            VipDto dto = new VipDto();
            dto.setStatus("0");
            BeanUtils.copyProperties(e, dto);
            Object str = redisTemplate.opsForValue().get(userKey);
            //可领取
            if (usr.getVipLevel() >= e.getLevel()) {
                dto.setStatus("2");
            }
            if (!ObjectUtils.isEmpty(str)) {
                WelfareRedis red = JSON.parseObject(str.toString(), WelfareRedis.class);
                red.getList().stream().forEach(re -> {
                    //已领取
                    if (re == e.getLevel()) {
                        dto.setStatus("1");
                    }
                });
            } else {
                WelfareRedis red = new WelfareRedis();
                red.setUserId(usr.getId());
                red.setList(new ArrayList<>());
                redisTemplate.opsForValue().set(userKey, JSON.toJSON(red));
            }
            //下一等级差额
            if (nextLevel == e.getLevel()) {
                returnDto.setNextCount(e.getThreshold().subtract(returnDto.getCount()));
            }
            listVip.add(dto);
        }
        returnDto.setList(listVip);
        return returnDto;
    }

    @Override
    public BigDecimal getVipWelfare(User usr, int lv) {
        Vip vip = vipservice.getVipBylv(lv);
        String userKey = "UserVip|" + usr.getId();
        Object str = redisTemplate.opsForValue().get(userKey);
        if (!ObjectUtils.isEmpty(str)) {
            WelfareRedis red = JSON.parseObject(str.toString(), WelfareRedis.class);
            if (red.getList().stream().anyMatch(m -> m.equals(lv))) {
                log.error("用户：{}的vip等级为{}的奖励已经领取过，请勿重复操作！", usr.getId(), lv);
            } else {
                //更新余额
                UserRewardLogs rewardLog = UserRewardLogs.builder()
                        .bean(vip.getPacket())
                        .type(7)//vip升级红包
                        .nextUserId(usr.getId())
                        .build();
                beanrecordservice.saveRewardLogs(rewardLog);
                User us = userservice.getUserById(usr.getId());
                //打到账户
                us.setBean(usr.getBean().add(vip.getPacket()));
                userservice.updateUser(us);
            }
            List<Integer> list = red.getList();
            list.add(lv);
            red.setList(list);
            redisTemplate.opsForValue().set(userKey, JSON.toJSON(red));
        }
        return vip.getPacket();
    }


    private int getReawardById(User usr, int id, String file, String userKey) {
        String jstr = this.getJsonForFile(file);
        List<RechangeWelfare> listLucky = JSON.parseArray(jstr, RechangeWelfare.class);
        for (RechangeWelfare e : listLucky) {
            if (e.getId() == id) {
                String[] reward = e.getReward().split("-");
                Random random = new Random();
                int number = random.nextInt(Integer.parseInt(reward[1]) - Integer.parseInt(reward[0]) + 1) + Integer.parseInt(reward[0]);
                log.info("==========领取福利金币：{}=================================", number);
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
                        log.info("==========用户领取前金币：{}=================================", usr.getBean());
                        BigDecimal balance = usr.getBean().add(new BigDecimal(number));
                        userservice.updateBean(balance, usr.getId());
                        log.info("==========用户领取福利后金币：{}=================================", balance);
                        List<Integer> list = red.getList();
                        list.add(id);
                        red.setList(list);
                        Long expire = redisTemplate.getExpire(userKey, TimeUnit.SECONDS);
                        redisTemplate.opsForValue().set(userKey, JSON.toJSON(red),expire,TimeUnit.SECONDS);
//                        Object ostr = redisTemplate.opsForValue().get("UserWelfare|Day" + usr.getId());
//                        int count = 0;
//                        if (!ObjectUtils.isEmpty(ostr)) {
//                            count = Integer.parseInt(ostr.toString());
//                        }
//                        redisTemplate.opsForValue().set("UserWelfare|Day" + usr.getId(), count + number, time, TimeUnit.SECONDS);
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
