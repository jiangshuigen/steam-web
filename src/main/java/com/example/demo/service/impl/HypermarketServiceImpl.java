package com.example.demo.service.impl;

import com.example.demo.config.Constant;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.WebBoxAwardsQuery;
import com.example.demo.entity.BoxAwards;
import com.example.demo.entity.BoxRecords;
import com.example.demo.mapper.LuckyBoxMapper;
import com.example.demo.service.BoxRecordService;
import com.example.demo.service.HypermarketService;
import com.example.demo.service.UserService;
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

    @Override
    public PageInfo<BoxAwards> getAwardList(WebBoxAwardsQuery query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<BoxAwards> list = mapper.getSellAwardList(query);
        PageInfo<BoxAwards> listInfo = new PageInfo<>(list);
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
                BoxAwards boxawards = mapper.getBoxAwardById(id);
                if (ObjectUtils.isEmpty(boxawards)) {
                    throw new Exception("商品ID有误");
                }
                cost = cost.add(boxawards.getBean());
                BoxRecords record = BoxRecords.builder()
                        .getUserId(user.getId())
                        .userId(user.getId())
                        .boxId(boxawards.getBoxId())
                        .boxName("商城购买")
                        .boxBean(boxawards.getBean())
                        .boxAwardId(boxawards.getId())
                        .name(boxawards.getName())
                        .hashName(boxawards.getHashName())
                        .cover(boxawards.getCover())
                        .dura(boxawards.getDura())
                        .lv(boxawards.getLv())
                        .bean(boxawards.getBean())
                        .maxT(new BigDecimal(0))
                        .code(this.getCode())
                        .uuid(UUID.randomUUID().toString())
                        .type(1)
                        .build();
                reList.add(record);
            }
            //判断余额
            if (cost.compareTo(user.getBean()) == 1) {
                throw new Exception("余额不足");
            }
            BigDecimal balance = user.getBean().subtract(cost);
            //扣除金币
            userservice.updateBean(balance, user.getId());
            return boxrecordservice.saveBoxRecord(reList);
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
