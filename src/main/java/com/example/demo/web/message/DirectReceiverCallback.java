package com.example.demo.web.message;

import com.example.demo.config.Constant;
import com.example.demo.dto.BasePage;
import com.example.demo.entity.BeanRecord;
import com.example.demo.entity.User;
import com.example.demo.entity.Vip;
import com.example.demo.service.BeanRecordService;
import com.example.demo.service.UserService;
import com.example.demo.service.VipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

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

    @RabbitHandler(isDefault = true)
    @Transactional
    public void process(String orderId) {
        log.info("=========DirectReceiverCallback  orderId is {}==================", orderId);
        BeanRecord record = beanrecordservice.queryBeanRecordsByCode(orderId);
        //Vip等级调整
        BasePage page = new BasePage();
        page.setPageNo(1);
        page.setPageSize(50);
        List<Vip> list = vipservice.getVipListByPage(page).getList();
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
        User user = userservice.getUserById(record.getUserId());
        user.setVipLevel(lv);
        user.setBean(user.getBean().add(record.getBean()));
        userservice.updateUser(user);
        log.info("===========充值到账=====================");
    }
}
