package com.example.demo.web.message;

import com.alibaba.fastjson.JSON;
import com.example.demo.config.Constant;
import com.example.demo.dto.BasePage;
import com.example.demo.entity.*;
import com.example.demo.service.BeanRecordService;
import com.example.demo.service.PromotionLevelService;
import com.example.demo.service.UserService;
import com.example.demo.service.VipService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Slf4j
@RabbitListener(queues = Constant.ORDER_CALLBACK_MESSAGE)
public class DirectReceiverCallback {

    @Resource
    private BeanRecordService beanrecordservice;
    @Resource
    private VipService vipservice;
    @Resource
    private UserService userservice;
    @Resource
    private PromotionLevelService promotionlevelservice;
    @Autowired
    private RedisTemplate redisTemplate;

    @RabbitHandler(isDefault = true)
    @Transactional(rollbackFor = Exception.class)
    public void process(String orderId) {
        log.info("=========DirectReceiverCallback  orderId is {}==================", orderId);
        BeanRecord record = beanrecordservice.queryBeanRecordsByCode(orderId);
        if (record.getStatus() == 1) {
            log.info("=====order status is pay ： ======{}", record.getStatus() == 1);
            return;
        }
        //Vip等级调整
        BasePage page = new BasePage();
        page.setPageNo(1);
        page.setPageSize(50);
        User user = userservice.getUserById(record.getUserId());
        List<Vip> list = vipservice.getVipListByPage(page).getList();
        BigDecimal rebate = new BigDecimal(0);
        for (Vip vip : list) {
            if (user.getVipLevel() == vip.getLevel()) {
                //充值返利
                rebate = record.getBean().multiply(vip.getRebate().divide(new BigDecimal(100)));
                UserRewardLogs rewardLogs = UserRewardLogs.builder()
                        .bean(rebate)
                        .type(4)//累计充值反佣
                        .userId(user.getId())
                        .build();
                beanrecordservice.saveRewardLogs(rewardLogs);
                //查询上一家
                if (!ObjectUtils.isEmpty(user.getInviterId())) {
                    User invUser = userservice.getUserById(user.getInviterId());
                    List<PromotionLevels> promotionlist = promotionlevelservice.getLevelList();
                    for (PromotionLevels promotionLevels : promotionlist) {
                        if (!ObjectUtils.isEmpty(invUser) && promotionLevels.getLevel() == invUser.getPromotionLevel()) {
                            BigDecimal balance = record.getBean().multiply(promotionLevels.getRebate().divide(new BigDecimal(100)));
                            UserRewardLogs rewardLog = UserRewardLogs.builder()
                                    .bean(balance)
                                    .type(2)//下级充值奖励
                                    .nextUserId(user.getId())
                                    .userId(invUser.getId())
                                    .chargeBean(rebate)
                                    .build();
                            beanrecordservice.saveRewardLogs(rewardLog);
                            //推广等级变更
                            BigDecimal allcount = beanrecordservice.queryPromotionAllBeanRecords(user.getInviterId());
                            List<PromotionLevels> InfoList = promotionlist.stream().sorted(Comparator.comparing(PromotionLevels::getLevel).reversed()).collect(Collectors.toList());
                            log.info("InfoList is {}", JSON.toJSON(InfoList));
                            for (PromotionLevels promotionlevels : InfoList) {
                                if (allcount.compareTo(promotionlevels.getInviteTotal()) > 0) {
                                    log.info("============下级累计充值金额：{}=======", allcount);
                                    //修改用户推广等级
                                    invUser.setPromotionLevel(promotionlevels.getLevel());
                                    break;
                                }
                            }
                            //打到账户
                            invUser.setBean(invUser.getBean().add(balance));
                            userservice.updateUser(invUser);
                        }
                    }
                }
                break;
            }
        }
        //查询累计充值
        BigDecimal bg = beanrecordservice.queryAllBeanRecords(record.getUserId());
        //第一次充值》100 奖励15
        if (bg.compareTo(new BigDecimal(0)) == 0) {
            if (record.getBean().compareTo(new BigDecimal(99.99)) == 1) {
                user.setBean(user.getBean().add(new BigDecimal(15)));
            }
        }
        List<Vip> InfoList = list.stream().sorted(Comparator.comparing(Vip::getLevel).reversed()).collect(Collectors.toList());
        int lv = 0;
        for (Vip vip : InfoList) {
            if (bg.compareTo(vip.getThreshold()) > 0) {
                //修改用户vip等级
                lv = vip.getLevel();
                break;
            }
        }
        user.setVipLevel(lv);
        user.setTruePay(bg);
        user.setBean(user.getBean().add(record.getBean()).add(rebate));
        userservice.updateUser(user);
        log.info("===========充值到账=====================");

        try {
            //计算失效时间
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
            String userKey = "UserRecharge|Day" + user.getId();
            this.setUserKey(userKey, record.getBean(), user.getId(), time);
            log.info("===========充值进度增加=====================");
        } catch (Exception e) {
            log.error("===========每日充值进度跟新失败=====================");
            e.printStackTrace();
        }

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
            String userKey = "UserRecharge|Month" + user.getId();
            this.setUserKey(userKey, record.getBean(), user.getId(), time);
        } catch (ParseException e) {
            log.error("===========每月充值进度跟新失败=====================");
            e.printStackTrace();
        }
        log.info("==============更新订单状态===============");
        beanrecordservice.updateBeanRecords(orderId);
    }

    private void setUserKey(String userKey, BigDecimal bean, int userId, long time) {
        Object str = redisTemplate.opsForValue().get(userKey);
        WelfareRedis red = null;
        if (!ObjectUtils.isEmpty(str)) {
            red = JSON.parseObject(str.toString(), WelfareRedis.class);
            red.setCost(red.getCost() + bean.intValue());
        } else {
            red = new WelfareRedis();
            red.setCost(bean.intValue());
            red.setUserId(userId);
            red.setList(new ArrayList<>());
        }
        redisTemplate.opsForValue().set(userKey, JSON.toJSON(red), time, TimeUnit.SECONDS);
        log.info("===========充值进度增加=====================");
    }
}
