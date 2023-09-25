package com.example.demo.service.impl;

import com.example.demo.config.Constant;
import com.example.demo.dto.SellerRecordQuery;
import com.example.demo.dto.UUawardsDto;
import com.example.demo.dto.UUawardsQuery;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.BoxRecords;
import com.example.demo.entity.UUSaleRsponse;
import com.example.demo.entity.User;
import com.example.demo.enumpakage.Dura;
import com.example.demo.mapper.LuckyBoxMapper;
import com.example.demo.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class HypermarketServiceImpl implements HypermarketService {
    @Resource
    private LuckyBoxMapper mapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private BoxRecordService boxrecordservice;
    @Resource
    private UserService userservice;
    @Resource
    private DeliveryRecordService deliveryrecordservice;
    @Resource
    private UUPService uupservice;


    @Override
    public PageInfo<UUawardsDto> getAwardList(UUawardsQuery query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<UUawardsDto> list = mapper.getSellAwardList(query);
        PageInfo<UUawardsDto> listInfo = new PageInfo<>(list);
        return listInfo;
    }

    @Override
    public int buyAwards(HttpServletRequest request, int[] ids) throws Exception {
        List<BoxRecords> reList = new ArrayList<>();
        //获取session
        HttpSession session = request.getSession();
        UserDto user = (UserDto) session.getAttribute(Constant.USER_INFO);
        if (!ObjectUtils.isEmpty(user)) {
            BigDecimal cost = new BigDecimal(0);
            for (int id : ids) {
                UUawardsDto boxawards = mapper.getSellAwardById(id);
                if (ObjectUtils.isEmpty(boxawards)) {
                    throw new Exception("商品ID有误");
                }
                cost = cost.add(boxawards.getBean());
                BoxRecords record = BoxRecords.builder()
                        .getUserId(user.getId())
                        .userId(user.getId())
                        .boxId(0)
                        .boxName("商城购买")
                        .boxBean(boxawards.getBean())
                        .boxAwardId(boxawards.getId())
                        .name(boxawards.getName())
                        .hashName(boxawards.getMarketHashName())
                        .cover(boxawards.getImageUrl())
                        .dura(this.getValue(boxawards.getDura()))
                        .lv(0)
                        .bean(boxawards.getBean())
                        .maxT(new BigDecimal(0))
                        .code(this.getCode())
                        .uuid(UUID.randomUUID().toString())
                        .type(1)
                        .build();
                reList.add(record);
            }
            User uus = userservice.getUserById(user.getId());
            //判断余额
            if (cost.compareTo(uus.getBean()) == 1) {
                throw new Exception("余额不足");
            }
            BigDecimal balance = uus.getBean().subtract(cost);
            //扣除金币
            userservice.updateBean(balance, user.getId());
            return boxrecordservice.saveBoxRecord(reList);
        }
        return 0;
    }

    @Override
    public int reloadAwardsPrice(Integer id) {
        UUawardsDto uUawardsDto = mapper.getSellAwardById(id);
        if (!ObjectUtils.isEmpty(uUawardsDto)) {
            SellerRecordQuery query = new SellerRecordQuery();
            query.setTemplateHashName(uUawardsDto.getMarketHashName());
            UUSaleRsponse res = deliveryrecordservice.getSellList(query);
            if (!ObjectUtils.isEmpty(res)) {
                if (res.getSalecommodityresponse().getReferencePrice() != null &&
                        res.getSalecommodityresponse().getReferencePrice().compareTo(uUawardsDto.getBean()) != 0) {
                    log.info("======id为：{}=====价格：{} 手动同步更新为：{}===============================", uUawardsDto.getId(), uUawardsDto.getBean(), res.getSalecommodityresponse().getReferencePrice());
                    uUawardsDto.setBean(res.getSalecommodityresponse().getReferencePrice());
                    uupservice.updateAwardsBean(uUawardsDto);
                }
            }
        }
        return 1;
    }

    private int getValue(String msg) {
        for (Dura value : Dura.values()) {
            if (value.getMsg().equals(msg)) {
                return value.getCode();
            }
        }
        return 0;
    }

    private String getCode() {
        Object ob = redisTemplate.opsForValue().get("OrderNo-");
        String lock = "";
        if (!ObjectUtils.isEmpty(ob)) {
            lock = (String) ob;
        }
        if (StringUtil.isNullOrEmpty(lock)) {
            lock = "1";
        } else {
            int num = Integer.parseInt(lock) + 1;
            if ((num < 10)) {
                lock = String.valueOf(num);
            } else {
                lock = "1";
            }
        }
        redisTemplate.opsForValue().set("OrderNo-", lock);
        //时间（精确到毫秒）
        DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String localDate = LocalDateTime.now().format(ofPattern);
        String orderNum = localDate + lock;
        log.info("orderNum===========" + orderNum);
        return orderNum;
    }
}
