package com.example.demo.web.message;

import com.example.demo.config.Constant;
import com.example.demo.dto.BasePage;
import com.example.demo.entity.*;
import com.example.demo.service.BeanRecordService;
import com.example.demo.service.PromotionLevelService;
import com.example.demo.service.UserService;
import com.example.demo.service.VipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
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

    @RabbitHandler(isDefault = true)
    @Transactional
    public void process(String orderId) {
        log.info("=========DirectReceiverCallback  orderId is {}==================", orderId);
        BeanRecord record = beanrecordservice.queryBeanRecordsByCode(orderId);
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
                        if (promotionLevels.getLevel() == invUser.getPromotionLevel()) {
                            UserRewardLogs rewardLog = UserRewardLogs.builder()
                                    .bean(rebate.multiply(promotionLevels.getRebate().divide(new BigDecimal(100))))
                                    .type(2)//下级充值奖励
                                    .nextUserId(user.getId())
                                    .userId(invUser.getId())
                                    .chargeBean(rebate)
                                    .build();
                            beanrecordservice.saveRewardLogs(rewardLog);
                        }
                    }
                }
                break;
            }
        }
        //查询累计充值
        BigDecimal bg = beanrecordservice.queryAllBeanRecords(record.getUserId());
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
        user.setBean(user.getBean().add(record.getBean()).add(rebate));
        userservice.updateUser(user);
        log.info("===========充值到账=====================");


    }
}
