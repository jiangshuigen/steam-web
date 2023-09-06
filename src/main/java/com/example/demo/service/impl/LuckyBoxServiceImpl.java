package com.example.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.demo.dto.*;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        List<AwardTypes> list = mapper.getTypeList();
        List<AwardTypes> reList = list.stream().filter(e -> e.getUuWeaponId() <= 0).collect(Collectors.toList());
        reList.stream().forEach(e -> {
            List<AwardTypes> liste = new ArrayList<>();
            for (AwardTypes awardTypes : list) {
                if (e.getUuTypeId() == awardTypes.getUuTypeId() && awardTypes.getUuWeaponId() > 0) {
                    liste.add(awardTypes);
                }
            }
            e.setList(liste);
        });
        return reList;
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
        long begin = System.currentTimeMillis();
        list.stream().forEach(e -> {
            //普通
            Object ob = redisTemplate.opsForValue().get("LUCKBOX|0|" + e.getId());
            if (!ObjectUtils.isEmpty(ob)) {
                BigDecimal bean = (BigDecimal) ob;
                e.setLeaveBean(bean);
            } else {
                e.setLeaveBean(new BigDecimal(0));
            }
            //主播
            Object obAnchor = redisTemplate.opsForValue().get("LUCKBOX|1|" + e.getId());
            if (!ObjectUtils.isEmpty(obAnchor)) {
                BigDecimal bean = (BigDecimal) obAnchor;
                e.setLeaveBeanAnchor(bean);
            } else {
                e.setLeaveBeanAnchor(new BigDecimal(0));
            }
        });
        long num = System.currentTimeMillis() - begin;
        log.info("+++++++++++++耗时++++++++++++" + num);
        PageInfo<BoxAwards> listInfo = new PageInfo<>(list);
        return listInfo;
    }

    @Override
    public int updateAward(BoxAwards award) {
        return mapper.updateAward(award);
    }

    @Override
    public PageInfo<LuckyBoxRecord> getLuckyBoxList(LuckyBoxRecordQuery query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<LuckyBoxRecord> list = luckyboxrecordmapper.getLuckyBoxList(query);
        PageInfo<LuckyBoxRecord> listInfo = new PageInfo<>(list);
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
        if (ObjectUtils.isEmpty(bx)) {
            throw new Exception("宝箱不存在");
        }
        BigDecimal cost = bx.getBean().multiply(new BigDecimal(openbox.getNumb())).setScale(2, BigDecimal.ROUND_DOWN);
        BigDecimal balance = user.getBean().subtract(cost);
        User us = userservice.getUserById(user.getId());
        if (us.getBean().compareTo(cost) == -1) {
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
        String listStr = (String) redisTemplate.opsForValue().get("BoxNumb-" + openbox.getBoxId() + "|" + user.getAnchor());
        if (StringUtil.isNullOrEmpty(listStr)) {
            this.nextTime(listAward, listRedis, openbox, user);
        } else {
            listRedis = JSON.parseArray(listStr, BoxAwards.class);
        }
        //开箱计数器
        Object ob = redisTemplate.opsForValue().get("BoxNumb-" + "|" + user.getAnchor() + openbox.getBoxId());
        int total = 0;
        for (BoxAwards ea : listAward) {
            if (user.getAnchor().equals(1)) {
                if (ea.getAnchorOdds() > 0) {
                    total += ea.getAnchorOdds();
                }
            } else {
                if (ea.getRealOdds() > 0) {
                    total += ea.getRealOdds();
                }
            }
        }
        int boxNumb = 0;
        if (!ObjectUtils.isEmpty(ob)) {
            boxNumb = (int) ob + openbox.getNumb();
            //再次判断是否开启下一论
            if (boxNumb > total) {
                //
                boxNumb = boxNumb - total;
            }
        } else {
            boxNumb = openbox.getNumb();
        }

        //幸运区间
        String jsonStr = user.getAnchor().equals(1) ? bx.getLuckIntervalAnchor() : bx.getLuckInterval();
        List<String> listLucky = JSON.parseArray(jsonStr, String.class);
        int begin = Integer.parseInt(listLucky.get(0));
        int end = Integer.parseInt(listLucky.get(1));
        List<BoxAwards> listLuckyWards = new ArrayList<>();
        //查询幸运物品并添加
        for (BoxAwards boxAwards : listAward) {
            if (boxAwards.getIsLuckyBox() == 1) {
                for (int i = 0; i < boxAwards.getLuckOdds(); i++) {
                    listLuckyWards.add(boxAwards);
                }
            }
        }
        //在幸运区间内添加到list
        if (boxNumb >= begin && boxNumb < end) {
            listRedis.addAll(listLuckyWards);
        } else if (boxNumb == end) {
            //幸运区间极限值 必中
            for (BoxAwards listLuckyward : listLuckyWards) {
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
        //洗牌
        Collections.shuffle(listRedis);
        int size = openbox.getNumb();
        for (int i = 0; i < size; i++) {
            if (listReturn.size() >= openbox.getNumb()) {
                break;
            }
            BoxRecords record = BoxRecords.builder()
                    .getUserId(user.getId())
                    .userId(user.getId())
                    .boxId(openbox.getBoxId())
                    .boxName(bx.getName())
                    .boxBean(bx.getBean())
                    .boxAwardId(listRedis.get(0).getId())
                    .name(listRedis.get(0).getName())
                    .hashName(listRedis.get(0).getHashName())
                    .cover(listRedis.get(0).getCover())
                    .dura(listRedis.get(0).getDura())
                    .lv(listRedis.get(0).getLv())
                    .bean(listRedis.get(0).getBean())
                    .maxT(new BigDecimal(0))
                    .code(this.getCode())
                    .uuid(UUID.randomUUID().toString())
                    .type(1)
                    .build();
            listReturn.add(record);
            //减库存
            AwardCountDto awardcountdto = new AwardCountDto();
            awardcountdto.setId(listRedis.get(0).getId());
            if (user.getAnchor().equals(1)) {
                if (listRedis.get(0).getIsLuckyBox() == 1) {
                    awardcountdto.setType(1);
                    int countRedis = (int) redisTemplate.opsForValue().get("BoxNumb|1|" + listRedis.get(0).getId() + "|");
                    awardcountdto.setNumb(countRedis - 1);
                    redisTemplate.opsForValue().set("BoxNumb|1|" + listRedis.get(0).getId() + "|", countRedis - 1);
                } else {
                    awardcountdto.setType(4);
                    int countRedis = (int) redisTemplate.opsForValue().get("BoxNumb|4|" + listRedis.get(0).getId() + "|");
                    awardcountdto.setNumb(countRedis - 1);
                    redisTemplate.opsForValue().set("BoxNumb|4|" + listRedis.get(0).getId() + "|", countRedis - 1);
                }
            } else {
                if (listRedis.get(0).getIsLuckyBox() == 1) {
                    awardcountdto.setType(2);
                    int countRedis = (int) redisTemplate.opsForValue().get("BoxNumb|1|" + listRedis.get(0).getId() + "|");
                    awardcountdto.setNumb(countRedis - 1);
                    redisTemplate.opsForValue().set("BoxNumb|1|" + listRedis.get(0).getId() + "|", countRedis - 1);
                } else {
                    awardcountdto.setType(3);
                    int countRedis = (int) redisTemplate.opsForValue().get("BoxNumb|3|" + listRedis.get(0).getId() + "|");
                    awardcountdto.setNumb(countRedis - 1);
                    redisTemplate.opsForValue().set("BoxNumb|3|" + listRedis.get(0).getId() + "|", countRedis - 1);
                }
            }
            try {
                log.info("库存处理开始==========：" + JSON.toJSONString(awardcountdto));
                mapper.updateAwardCount(awardcountdto);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("库存处理失败");
            }
            listRedis.remove(0);
            if (this.nextTime(listAward, listRedis, openbox, user)) {
                ob = boxNumb;
            }
        }
        String reds = JSON.toJSONString(listRedis);
        redisTemplate.opsForValue().set("BoxNumb-" + openbox.getBoxId() + "|" + user.getAnchor(), reds);
        //保存开箱记录
        boxrecordservice.saveBoxRecord(listReturn);
        log.info("箱子id======" + openbox.getBoxId() + "已开箱数目=======" + boxNumb);
        if (ObjectUtils.isEmpty(ob)) {
            redisTemplate.opsForValue().set("BoxNumb-" + "|" + user.getAnchor() + openbox.getBoxId(), openbox.getNumb());
        } else {
            redisTemplate.opsForValue().set("BoxNumb-" + "|" + user.getAnchor() + openbox.getBoxId(), boxNumb);
        }
        return listReturn;
    }

    @Override
    public PageInfo<BoxAwards> getWebAwardList(WebBoxAwardsQuery query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<BoxAwards> list = mapper.getWebAwardList(query);
        PageInfo<BoxAwards> listInfo = new PageInfo<>(list);
        return listInfo;
    }

    @Override
    public List<LuckyBoxRecord> getHistory(int awardId) {
        return luckyboxrecordmapper.getHistory(awardId);
    }

    @Override
    @Transactional
    public List<BoxAwards> openAward(OpenDto dto, UserDto user) throws Exception {
        List<BoxAwards> returnList = new ArrayList<>();
        List<BoxRecords> listReturn = new ArrayList<>();
        BoxAwards boxawards = mapper.getBoxAwardById(dto.getAwardId());
        String jsonStr = "";
        if (user.getAnchor() == 1) {
            jsonStr = boxawards.getLuckIntervalAnchor();
        } else {
            jsonStr = boxawards.getLuckInterval();
        }
        Object ob = redisTemplate.opsForValue().get("LUCKBOX|" + user.getAnchor() + "|" + dto.getAwardId());
        BigDecimal total = new BigDecimal(0);
        if (ObjectUtils.isEmpty(ob)) {
            //首次开箱
            log.info("++++++++++++++++首次开启饰品++++++++++++++++++++++");
            //计算额度
            List<String> listLucky = JSON.parseArray(jsonStr, String.class);
            int min = Integer.parseInt(listLucky.get(0));
            int max = Integer.parseInt(listLucky.get(1));
            int radom = (int) (min + Math.random() * (max - min + 1));
            BigDecimal maxD = new BigDecimal(max);
            BigDecimal aaRes = maxD.setScale(-(listLucky.get(1).length() - 1), BigDecimal.ROUND_DOWN);
            total = boxawards.getBean().divide(aaRes).multiply(new BigDecimal(radom)).setScale(2, BigDecimal.ROUND_DOWN);
            ;
            log.info("++++++++++" + dto.getAwardId() + "++的额度是：" + total + "++++++++++++++++++++++++++++");
        } else {
            total = (BigDecimal) ob;
        }
        //花费金额
        BigDecimal cost = boxawards.getBean().multiply(dto.getPercent().multiply(new BigDecimal(1.1))).setScale(2, BigDecimal.ROUND_DOWN);
        if (cost.compareTo(new BigDecimal(3)) == -1) {
            throw new Exception("金额小于3币");
        }
        User us = userservice.getUserById(user.getId());
        BigDecimal balance = us.getBean().subtract(cost);
        //扣除金币
        userservice.updateBean(balance, user.getId());
        LuckyBoxRecord record = null;
        //剩余额度
        if (total.compareTo(cost) == -1) {
            log.info("++++++++++" + dto.getAwardId() + "++的剩余额度是0 中奖++++++++++++++++++++++++++++");
            returnList.add(boxawards);
            //添加记录
            record = LuckyBoxRecord.builder()
                    .userId(user.getId())
                    .boxBean(cost)
                    .percent(dto.getPercent())
                    .awardId(dto.getAwardId())
                    .targetName(boxawards.getName())
                    .targetCover(boxawards.getCover())
                    .awardDura(boxawards.getDura())
                    .awardLv(boxawards.getLv())
                    .targetValue(boxawards.getBean())//目标价值
                    .getAwardId(boxawards.getId())
                    .obtainName(boxawards.getName())
                    .obtainHashName(boxawards.getHashName())
                    .obtainCover(boxawards.getCover())
                    .getAwardDura(boxawards.getDura())
                    .getAwardLv(boxawards.getLv())
                    .obtainValue(boxawards.getBean())
                    .build();
            //中奖后额度复位
            redisTemplate.delete("LUCKBOX|" + user.getAnchor() + "|" + dto.getAwardId());
        } else {
            BigDecimal least = total.subtract(cost);
            redisTemplate.opsForValue().set("LUCKBOX|" + user.getAnchor() + "|" + dto.getAwardId(), least);
            log.info("++++++++++" + dto.getAwardId() + "++的剩余额度是" + least + "++++++++++++++++++++++++++++");
            int id = 1448;
            Object leaveId = redisTemplate.opsForValue().get("LUCKBOX|" + user.getAnchor() + "|DEFAULT");
            if (!ObjectUtils.isEmpty(leaveId)) {
                id = (int) leaveId;
            }
            //指定一条记录
            BoxAwards leaveDto = mapper.getBoxAwardById(id);
            returnList.add(leaveDto);
            //添加记录
            record = LuckyBoxRecord.builder()
                    .userId(user.getId())
                    .boxBean(cost)
                    .percent(dto.getPercent())
                    .awardId(dto.getAwardId())
                    .targetName(boxawards.getName())
                    .targetCover(boxawards.getCover())
                    .awardDura(boxawards.getDura())
                    .awardLv(boxawards.getLv())
                    .targetValue(boxawards.getBean())//目标价值
                    .getAwardId(leaveDto.getId())
                    .obtainName(leaveDto.getName())
                    .obtainCover(leaveDto.getCover())
                    .getAwardDura(leaveDto.getDura())
                    .getAwardLv(leaveDto.getLv())
                    .obtainHashName(leaveDto.getHashName())
                    .obtainValue(leaveDto.getBean())
                    .build();
        }
        int i = luckyboxrecordmapper.saveRecord(record);
        BoxRecords bxrecord = BoxRecords.builder()
                .getUserId(user.getId())
                .userId(user.getId())
                .boxId(0)
                .boxName("幸运开箱")
                .boxBean(cost)
                .boxAwardId(record.getAwardId())
                .name(record.getObtainName())
                .hashName(record.getObtainHashName())
                .cover(record.getObtainCover())
                .dura(record.getGetAwardDura())
                .lv(record.getAwardLv())
                .bean(record.getObtainValue())
                .maxT(new BigDecimal(0))
                .code(this.getCode())
                .uuid(UUID.randomUUID().toString())
                .type(4)
                .build();
        listReturn.add(bxrecord);
        //保存开箱记录
        boxrecordservice.saveBoxRecord(listReturn);
        return returnList;
    }

    private boolean nextTime(List<BoxAwards> listAward, List<BoxAwards> listRedis, OpenBox openbox, User user) {
        //判断是否下一轮
        if (CollectionUtils.isEmpty(listRedis)) {
            log.info("==============新一轮======清理缓存==========");
            redisTemplate.opsForValue().set("BoxNumb-" + "|" + user.getAnchor() + "|" + openbox.getBoxId(), "");
            for (BoxAwards ea : listAward) {
                if (user.getAnchor().equals(1)) {
                    if (ea.getAnchorOdds() > 0) {
                        int j = 0;
                        while (j < ea.getAnchorOdds()) {
                            j++;
                            listRedis.add(ea);
                        }
                    }
                } else {
                    if (ea.getRealOdds() > 0) {
                        int j = 0;
                        while (j < ea.getRealOdds()) {
                            j++;
                            listRedis.add(ea);
                        }
                    }
                }
            }
            return true;
        }
        return false;
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
