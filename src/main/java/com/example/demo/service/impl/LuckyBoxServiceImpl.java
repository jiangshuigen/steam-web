package com.example.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.demo.dto.BoxAwardsQuery;
import com.example.demo.dto.LuckyBboxRecordQuery;
import com.example.demo.dto.OpenBox;
import com.example.demo.entity.*;
import com.example.demo.mapper.LuckyBoxMapper;
import com.example.demo.mapper.LuckyBoxRecordMapper;
import com.example.demo.service.BoxRecordService;
import com.example.demo.service.BoxService;
import com.example.demo.service.LuckyBoxService;
import com.example.demo.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class LuckyBoxServiceImpl implements LuckyBoxService {

    @Resource
    private LuckyBoxMapper mapper;
    @Resource
    private LuckyBoxRecordMapper luckyboxrecordmapper;
    @Resource
    private BoxService boxservice;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private BoxRecordService boxrecordservice;
    @Autowired
    private UserService userservice;

    @Override
    public List<AwardTypes> getTypeList() {
        return mapper.getTypeList();
    }

    @Override
    public AwardTypes getTypeById(int id) {
        return mapper.getTypeById(id);
    }

    @Override
    public int updateType(AwardTypes type) {
        return mapper.updateType(type);
    }

    @Override
    public int saveType(AwardTypes type) {
        return mapper.saveType(type);
    }

    @Override
    public PageInfo<BoxAwards> getAwardList(BoxAwardsQuery query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<BoxAwards> list = mapper.getAwardList(query);
        PageInfo<BoxAwards> listInfo = new PageInfo<>(list);
        return listInfo;
    }

    @Override
    public int updateAward(BoxAwards award) {
        return mapper.updateAward(award);
    }

    @Override
    public PageInfo<LuckyBboxRecord> getLuckyBoxList(LuckyBboxRecordQuery query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<LuckyBboxRecord> list = luckyboxrecordmapper.getLuckyBoxList(query);
        PageInfo<LuckyBboxRecord> listInfo = new PageInfo<>(list);
        return listInfo;
    }

    @Override
    public int deleteById(int id) {
        return mapper.deleteById(id);
    }

    @Override
    public List<BoxAwards> getIndexBoxList(int boxId) {
        return mapper.getIndexBoxList(boxId);
    }

    @Override
    @Transactional
    public List<BoxRecords> openBox(OpenBox openbox, User user) throws Exception {
        //计算金币
        Box bx = boxservice.getBoxById(openbox.getBoxId());
        if(ObjectUtils.isEmpty(bx)){
            throw new Exception("宝箱不存在");
        }
        BigDecimal cost = bx.getBean().multiply(new BigDecimal(openbox.getNumb()));
        BigDecimal balance = user.getBean().subtract(cost);
        if (user.getBean().compareTo(cost) == -1) {
            throw new Exception("请充值");
        } else {
            //扣除金币
            userservice.updateBean(balance, user.getId());
        }
        List<BoxRecords> listReturn = new ArrayList<>();
        //普通开箱
        List<BoxAwards> listRedis = new ArrayList<>();
        //查询盒子里的武器列表
        List<BoxAwards> listAward = mapper.getIndexBoxList(openbox.getBoxId());
        //获取redis数据
        String listStr = (String) redisTemplate.opsForValue().get(openbox.getBoxId() + "|" + user.getAnchor());
        if (StringUtil.isNullOrEmpty(listStr)) {
            for (BoxAwards e : listAward) {
                if (e.getRealOdds() > 0) {
                    int i = 0;
                    while (i < e.getRealOdds()) {
                        i++;
                        listRedis.add(e);
                    }
                }
            }
        } else {
            listRedis = JSON.parseArray(listStr, BoxAwards.class);
        }
        //开箱计数器
        Object ob = redisTemplate.opsForValue().get("BoxNumb-" + "|" + user.getAnchor() + openbox.getBoxId());
        //幸运区间
        if (!StringUtil.isNullOrEmpty(bx.getLuckInterval())) {
            String jsonStr = user.getAnchor().equals(1) ? bx.getLuckIntervalAnchor() : bx.getLuckInterval();
            List<String> listLucky = JSON.parseArray(jsonStr, String.class);
            int begin = Integer.parseInt(listLucky.get(0));
            int end = Integer.parseInt(listLucky.get(1));
            int boxNumb = ob != null ? (int) ob + openbox.getNumb() : openbox.getNumb();
            log.info("箱子id======" + openbox.getBoxId() + "已开箱数目=======" + boxNumb);
            List<BoxAwards> listLuckywards = new ArrayList<>();
            //查询幸运物品并添加
            for (BoxAwards boxAwards : listAward) {
                if (boxAwards.getIsLuckyBox() == 1) {
                    for (int i = 0; i < boxAwards.getLuckOdds(); i++) {
                        listLuckywards.add(boxAwards);
                    }
                }
            }
            //在幸运区间内添加到list
            if (boxNumb >= begin && boxNumb < end) {
                listRedis.addAll(listLuckywards);
            } else if (boxNumb == end) {
                //幸运区间极限值 必中
                for (BoxAwards listLuckyward : listLuckywards) {
                    BoxRecords record = BoxRecords.builder()
                            .getUserId(user.getId())
                            .userId(user.getId())
                            .boxId(openbox.getBoxId())
                            .boxName(bx.getName())
                            .boxBean(bx.getBean())
                            .boxAwardId(listLuckyward.getId())
                            .name(listLuckyward.getName())
                            .hashName(listLuckyward.getHashName())
                            .cover(listLuckyward.getCover())
                            .dura(listLuckyward.getDura())
                            .lv(listLuckyward.getLv())
                            .bean(listLuckyward.getBean())
                            .maxT(new BigDecimal(0))
                            .code(this.getCode())
                            .uuid(UUID.randomUUID().toString())
                            .type(1)
                            .build();
                    listReturn.add(record);
                }
            }
        }
        //洗牌
        Collections.shuffle(listRedis);
        for (int i = 0; i < openbox.getNumb(); i++) {
            if (listReturn.size() >= openbox.getNumb()) {
                break;
            }
            BoxRecords record = BoxRecords.builder()
                    .getUserId(user.getId())
                    .userId(user.getId())
                    .boxId(openbox.getBoxId())
                    .boxName(bx.getName())
                    .boxBean(bx.getBean())
                    .boxAwardId(listRedis.get(i).getId())
                    .name(listRedis.get(i).getName())
                    .hashName(listRedis.get(i).getHashName())
                    .cover(listRedis.get(i).getCover())
                    .dura(listRedis.get(i).getDura())
                    .lv(listRedis.get(i).getLv())
                    .bean(listRedis.get(i).getBean())
                    .maxT(new BigDecimal(0))
                    .code(this.getCode())
                    .uuid(UUID.randomUUID().toString())
                    .type(1)
                    .build();
            listReturn.add(record);
            listRedis.remove(i);
        }
        String reds = JSON.toJSONString(listRedis);
        redisTemplate.opsForValue().set(openbox.getBoxId() + "|" + user.getAnchor(), reds);
        //保存开箱记录
        boxrecordservice.saveBoxRecord(listReturn);
        if (ObjectUtils.isEmpty(ob)) {
            redisTemplate.opsForValue().set("BoxNumb-" + "|" + user.getAnchor() + openbox.getBoxId(), openbox.getNumb());
        } else {
            int boxNumb = (int) ob + openbox.getNumb();
            redisTemplate.opsForValue().set("BoxNumb-" + "|" + user.getAnchor() + openbox.getBoxId(), boxNumb);
        }

        return listReturn;
    }


    public String getCode() {
        String lock = (String) redisTemplate.opsForValue().get("OrderNo-");
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
