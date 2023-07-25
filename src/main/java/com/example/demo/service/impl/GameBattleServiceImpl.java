package com.example.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.mapper.GameBattleMapper;
import com.example.demo.mapper.LuckyBoxMapper;
import com.example.demo.service.BoxRecordService;
import com.example.demo.service.GameBattleService;
import com.example.demo.service.UserService;
import com.example.demo.util.CodeUtils;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GameBattleServiceImpl implements GameBattleService {

    @Resource
    private GameBattleMapper gamebattlemapper;
    @Resource
    private LuckyBoxMapper mapper;
    @Resource
    private BoxRecordService boxrecordservice;
    @Resource
    private UserService userservice;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    @Transactional
    public int createEvent(GameArenasSaveDto info) {
        //计算金额
        BigDecimal total = new BigDecimal(0);
        for (Integer id : info.getListBox()) {
            total = total.add(gamebattlemapper.getTotalBean(id));
        }
        info.setTotalBean(total);
        int i = gamebattlemapper.createEvent(info);
        if (i > 0) {
            //记录箱子
            info.getListBox().stream().forEach(e -> {
                GameArenaBox bx = new GameArenaBox();
                bx.setBoxId(e);
                bx.setGameArenaId(info.getId());
                gamebattlemapper.insertArenaBox(bx);
            });
        }
        //更新对战用户
        GameArenaUsers us = new GameArenaUsers();
        us.setGameArenaId(info.getId());
        us.setSeat(0);
        us.setUserId(info.getCreateUserId());
        us.setWorth(info.getTotalBean());
        int numb = gamebattlemapper.insertArenaUsers(us);
        return numb;
    }

    @Override
    public PageInfo<GameArenasDto> getEventList(BasePage base) {
        PageHelper.startPage(base.getPageNo(), base.getPageSize());
        List<GameArenasDto> list = gamebattlemapper.getEventList(base);
        PageInfo<GameArenasDto> listInfo = new PageInfo<>(list);
        return listInfo;
    }

    @Override
    public GameArenasDto getGameArenasDetail(int id) {
        return gamebattlemapper.getGameArenasDetail(id);
    }

    @Override
    @Transactional
    public List<BattleDto> joinEvent(int id, UserDto user) throws Exception {
        //查询活动
        GameArenasDto dto = gamebattlemapper.getGameArenasDetail(id);
        if (dto.getStatus() == 2) {
            throw new Exception("活动已经结束");
        }
        //人员列表
        List<GameArenasUserDto> listUser = dto.getListUser();
        //判断余额
        if (dto.getTotalBean().compareTo(user.getBean()) == 1) {
            throw new Exception("余额不足");
        }
        if (!CollectionUtils.isEmpty(dto.getListUser())) {
            for (GameArenasUserDto gameArenasUserDto : dto.getListUser()) {
                if (gameArenasUserDto.getGameUserId() == user.getId()) {
                    throw new Exception("您已经加入，等待活动开始");
                }
            }
        }
        //判断是否满足开启条件
        int numb = (dto.getListUser().size() + 1);
        if (dto.getUserNum() < numb) {
            throw new Exception("活动已结束");
        } else {
            //人员加入
            GameArenaUsers us = new GameArenaUsers();
            us.setGameArenaId(dto.getId());
            us.setSeat(dto.getListUser().size());
            us.setUserId(user.getId());
            us.setWorth(dto.getTotalBean());
            GameArenasUserDto usDto = new GameArenasUserDto();
            usDto.setGameUserId(user.getId());
            usDto.setGameAvatar(user.getAvatar());
            usDto.setGameUserName(user.getName());
            listUser.add(usDto);
            gamebattlemapper.insertArenaUsers(us);
        }
        if (dto.getUserNum() == numb) {
            //所有玩家roll的列表记录
            List<BoxRecords> listReturn = new ArrayList<>();
            List<BattleDto> listBattle = new ArrayList<>();
            List<GameAwardRecords> gameawardrecords = new ArrayList<>();
            //对战开始
            Collections.shuffle(listUser);
            for (GameArenasUserDto gameArenasUserDto : listUser) {
                BattleDto battleDto = new BattleDto();
                battleDto.setUserId(gameArenasUserDto.getGameUserId());
                List<BoxRecords> listUserAWards = new ArrayList<>();
                for (GameArenasBoxDto gameArenasBoxDto : dto.getListBox()) {
                    //获取宝箱下的武器列表
                    List<BoxAwards> listAward = mapper.getIndexBoxList(gameArenasBoxDto.getBoxId());
                    //洗牌
                    Collections.shuffle(listAward);
                    for (BoxAwards boxAwards : listAward) {
                        BigDecimal beanCount = battleDto.getBean() == null ? boxAwards.getBean() : battleDto.getBean().add(boxAwards.getBean());
                        battleDto.setBean(beanCount);
                        BoxRecords record = BoxRecords.builder()
                                .getUserId(gameArenasUserDto.getGameUserId())
                                .userId(gameArenasUserDto.getGameUserId())
                                .boxId(boxAwards.getBoxId())
                                .boxName(gameArenasBoxDto.getBoxName())
                                .boxBean(dto.getTotalBean())
                                .boxAwardId(boxAwards.getId())
                                .name(boxAwards.getName())
                                .hashName(boxAwards.getHashName())
                                .cover(boxAwards.getCover())
                                .dura(boxAwards.getDura())
                                .lv(boxAwards.getLv())
                                .bean(boxAwards.getBean())
                                .maxT(new BigDecimal(0))
                                .code(this.getCode())
                                .uuid(UUID.randomUUID().toString())
                                .type(1)
                                .build();
                        listReturn.add(record);
                        listUserAWards.add(record);
                        GameAwardRecords rd = GameAwardRecords.builder()
                                .gameArenaId(id)
                                .status(1)
                                .awardId(boxAwards.getId())
                                .userId(gameArenasUserDto.getGameUserId())
                                .build();
                        gameawardrecords.add(rd);
                        break;
                    }
                }
                battleDto.setListAward(listUserAWards);
                listBattle.add(battleDto);
            }
            //计算奖品归属 如果两人比赛平局各自领取各自的物品 否则平局随机抽取胜利玩家
            boolean bl = listBattle.stream().allMatch(listBattle.get(0).getBean()::equals);
            List<Integer> winner = new ArrayList<>();
            if (bl && dto.getUserNum() == 2) {
                log.info("==========平局====================");
                //各自放入背包
                listBattle.stream().forEach(e -> {
                    e.setWin(1);
                    winner.add(e.getUserId());
                });
            } else if (bl && dto.getUserNum() != 2) {
                //随机抽取用户
                Collections.shuffle(listUser);
                listReturn.stream().forEach(e -> {
                    e.setGetUserId(listUser.get(0).getGameUserId());
                    e.setUserId(listUser.get(0).getGameUserId());
                });
                log.info("==========随机抽取用户 胜利者：" + listUser.get(0).getGameUserId() + "====================");
                winner.add(listUser.get(0).getGameUserId());
                listBattle.stream().forEach(e -> {
                    if (listUser.get(0).getGameUserId() == e.getUserId()) {
                        e.setWin(1);
                        e.setListAward(listReturn);
                    } else {
                        //其他玩家+0.01
                        e.setListAward(new ArrayList<>());
                        userservice.sendReward(e.getUserId());
                    }
                });

            } else {
                List<BattleDto> userInfoList = listBattle.stream().sorted(Comparator.comparing(BattleDto::getBean).reversed()).collect(Collectors.toList());
                log.info("排列顺序：" + JSON.toJSONString(userInfoList));
                listReturn.stream().forEach(e -> {
                    e.setGetUserId(userInfoList.get(0).getUserId());
                    e.setUserId(userInfoList.get(0).getUserId());
                });
                winner.add(userInfoList.get(0).getUserId());
                listBattle.stream().forEach(e -> {
                    if (e.getUserId() == userInfoList.get(0).getUserId()) {
                        e.setWin(1);
                        e.setListAward(listReturn);
                    } else {
                        e.setListAward(new ArrayList<>());
                        userservice.sendReward(e.getUserId());
                    }
                });
            }
            boxrecordservice.saveBoxRecord(listReturn);
            log.info("record 记录列表记录数目:" + gameawardrecords.size());
            gamebattlemapper.saveGameAwardRecords(gameawardrecords);
            dto.setStatus(2);
            log.info("winner is ======" + JSON.toJSONString(winner));
            Object[] s2 = winner.toArray();
            dto.setWinUserId(JSON.toJSONString(s2));
            dto.setDrawCode(CodeUtils.getCode());
            gamebattlemapper.update(dto);
            listBattle.stream().forEach(e -> {
                BigDecimal num = e.getListAward().stream().map(BoxRecords::getBean).reduce(BigDecimal.ZERO, BigDecimal::add);
                //记录榜单
                GameRanking ranking = gamebattlemapper.queryRankUserList(e.getUserId());
                if (ObjectUtils.isEmpty(ranking)) {
                    ranking = new GameRanking();
                    ranking.setUserId(e.getUserId());
                    ranking.setLostBean(new BigDecimal(0));
                    ranking.setIncome(new BigDecimal(0));
                    ranking.setExpend(new BigDecimal(0));
                    ranking.setWinRate(new BigDecimal(0));
                }
                if (num.compareTo(new BigDecimal(0)) == 1) {
                    e.setBean(num);
                    ranking.setTotal(ranking.getTotal() + 1);
                    ranking.setWin(ranking.getWin() + 1);
                    ranking.setIncome(ranking.getIncome().add(num));
                    ranking.setLostBean(ranking.getLostBean().add(num).subtract(dto.getTotalBean()));
                } else {
                    e.setBean(new BigDecimal(0.01));
                    ranking.setFail(ranking.getFail() + 1);
                    ranking.setIncome(ranking.getIncome());
                    ranking.setLostBean(ranking.getLostBean().subtract(dto.getTotalBean()));
                }
                BigDecimal winRate = new BigDecimal(ranking.getWin()).divide(new BigDecimal(ranking.getWin() + ranking.getFail()),2, BigDecimal.ROUND_HALF_UP);
                ranking.setWinRate(winRate);
                ranking.setExpend(ranking.getExpend().add(dto.getTotalBean()));
                if (ranking.getId() > 0) {
                    //修改
                    gamebattlemapper.updateRanking(ranking);
                }else {
                    gamebattlemapper.saveRanking(ranking);
                }
            });
            return listBattle;
        }
        return null;
    }

    @Override
    public PageInfo<GameArenasDto> getBattleList(BasePage query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<GameArenasDto> list = gamebattlemapper.getBattleList(query);
        PageInfo<GameArenasDto> listInfo = new PageInfo<>(list);
        return listInfo;
    }

    @Override
    public PageInfo<GameArenasDto> getMyBattleList(BattleQuery query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<GameArenasDto> list = gamebattlemapper.getMyBattleList(query);
        PageInfo<GameArenasDto> listInfo = new PageInfo<>(list);
        return listInfo;
    }

    @Override
    public List<GameRanking> getGameRankingList(GameRankingQuery query) {
        return gamebattlemapper.getGameRankingList(query);
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
