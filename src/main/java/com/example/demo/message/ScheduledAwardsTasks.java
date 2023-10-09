package com.example.demo.message;


import com.example.demo.dto.SellerRecordQuery;
import com.example.demo.dto.UUawardsDto;
import com.example.demo.entity.UUSaleRsponse;
import com.example.demo.service.DeliveryRecordService;
import com.example.demo.service.UUPService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
@Lazy(value = false)
public class ScheduledAwardsTasks {


    @Autowired
    private UUPService uupservice;

    @Autowired
    private DeliveryRecordService deliveryrecordservice;

    /**
     * 每隔三小时更新基础数据价格
     */
    @Scheduled(cron = "0 0 0/2 * * ?")
    public void updateAwards() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
        log.info("====开始：{}", dateFormat.format(new Date()));
        log.info("===========begin:定时任务开始：{}===============================", System.currentTimeMillis());
        List<UUawardsDto> list = uupservice.getAllUUAwardList();
        for (UUawardsDto uUawardsDto : list) {
            SellerRecordQuery query = new SellerRecordQuery();
            query.setTemplateHashName(uUawardsDto.getMarketHashName());
            UUSaleRsponse res = deliveryrecordservice.getSellList(query);
            if (!ObjectUtils.isEmpty(res)) {
                if (res.getSalecommodityresponse().getMinSellPrice() != null &&
                        res.getSalecommodityresponse().getMinSellPrice().compareTo(uUawardsDto.getBean()) != 0) {
                    BigDecimal cron = res.getSalecommodityresponse().getMinSellPrice().multiply(new BigDecimal(1.03)).setScale(2, BigDecimal.ROUND_HALF_UP);
                    log.info("======id为：{}=====价格：{} 更新为：{}===============================", uUawardsDto.getId(), uUawardsDto.getBean(), cron);
                    uUawardsDto.setBean(cron);
                    uupservice.updateAwardsBean(uUawardsDto);
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info("===========end:定时任务结束：{}===============================", System.currentTimeMillis());
        log.info("====结束：{}", dateFormat.format(new Date()));
    }
}