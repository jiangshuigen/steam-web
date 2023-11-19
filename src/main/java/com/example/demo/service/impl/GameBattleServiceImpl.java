package com.example.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.mapper.GameBattleMapper;
import com.example.demo.mapper.LuckyBoxMapper;
import com.example.demo.service.BoxRecordService;
import com.example.demo.service.GameBattleService;
import com.example.demo.service.UserService;
import com.example.demo.util.CodeUtils;
import com.example.demo.web.web.WebSocket;
import com.example.demo.web.web.WebSocketService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
    @Resource
    private WebSocket webSocket;
    @Resource
    private WebSocketService websocketservice;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createEvent(GameArenasSaveDto info, UserDto user) throws Exception {
        User uus = userservice.getUserById(user.getId());
        //计算金额
        BigDecimal total = new BigDecimal(0);
        for (Integer id : info.getListBox()) {
            total = total.add(gamebattlemapper.getTotalBean(id));
        }
        //判断余额
        if (total.compareTo(uus.getBean()) == 1) {
            throw new Exception("余额不足");
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
        //扣除费用

        BigDecimal balance = uus.getBean().subtract(total);
        //扣除金币
        userservice.updateBean(balance, info.getCreateUserId());
        //定时15分钟内未有用户加入则解散
        long tim = 15 * 60;
        redisTemplate.opsForValue().set("BATTLE|" + info.getId(), info.getId(), tim, TimeUnit.SECONDS);
        return info.getId();
    }

    @Override
    public PageInfo<GameArenasDto> getEventList(BattleWebQuery base) {
        PageHelper.startPage(base.getPageNo(), base.getPageSize());
        List<GameArenasDto> list = gamebattlemapper.getEventList(base);
        PageInfo<GameArenasDto> listInfo = new PageInfo<>(list);
        return listInfo;
    }

    @Override
    public GameArenasDto getGameArenasDetail(int id) {
        GameArenasDto dto = gamebattlemapper.getGameArenasDetail(id);
        //本局游戏奖品列表
        List<BoxRecords> recordList = gamebattlemapper.getBoxRecordList(id);
        dto.setRecordList(recordList);
        recordList.stream().forEach(re -> {
            dto.getListBox().stream().forEach(e -> {
                List<BoxAwards> listAward = mapper.getIndexBoxList(e.getBoxId());
                e.setListAward(listAward);
                //判定武器批次
                listAward.stream().forEach(award -> {
                    if (re.getBoxAwardId() == award.getId()) {
                        re.setBoxId(e.getBoxId());
                    }
                });
            });
        });
        //
        for (GameArenasUserDto gameArenasUserDto : dto.getListUser()) {
            List<BoxRecords> reList = new ArrayList<>();
            for (BoxRecords records : recordList) {
                if (records.getUserId() == gameArenasUserDto.getGameUserId()) {
                    reList.add(records);
                }
            }
            reList.sort(Comparator.comparing(BoxRecords::getCreatedAt));
            gameArenasUserDto.setRecordList(reList);
            List<Integer> listWinner = JSON.parseArray(dto.getWinUserId(), int.class);
            if (!CollectionUtils.isEmpty(listWinner) && listWinner.size() > 1) {
                gameArenasUserDto.setGameBean(reList.stream().map(BoxRecords::getBean).reduce(BigDecimal.ZERO, BigDecimal::add));
            } else if (!CollectionUtils.isEmpty(listWinner) && listWinner.size() == 1 && listWinner.get(0) == gameArenasUserDto.getGameUserId()) {
                gameArenasUserDto.setGameBean(recordList.stream().map(BoxRecords::getBean).reduce(BigDecimal.ZERO, BigDecimal::add));
            } else {
                gameArenasUserDto.setGameBean(new BigDecimal(0));
            }

        }
        dto.setWinnerBean(dto.getRecordList().stream().map(BoxRecords::getBean).reduce(BigDecimal.ZERO, BigDecimal::add));
        return dto;
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
            //扣除费用
            User uus = userservice.getUserById(user.getId());
            BigDecimal balance = uus.getBean().subtract(dto.getTotalBean());
            //扣除金币
            userservice.updateBean(balance, user.getId());

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
                    List<BoxAwards> listAwardRoll = new ArrayList<>();
                    for (BoxAwards boxAwards : listAward) {
                        for (int i = 0; i < boxAwards.getRealOdds(); i++) {
                            listAwardRoll.add(boxAwards);
                        }
                    }
                    //洗牌
                    Collections.shuffle(listAwardRoll);
                    for (BoxAwards boxAwards : listAwardRoll) {
                        BigDecimal beanCount = battleDto.getBean() == null ? boxAwards.getBean() : battleDto.getBean().add(boxAwards.getBean());
                        battleDto.setBean(beanCount);
                        BoxRecords record = BoxRecords.builder()
                                .getUserId(gameArenasUserDto.getGameUserId())
                                .userId(gameArenasUserDto.getGameUserId())
                                .boxId(boxAwards.getBoxId())
                                .boxName("盲盒对战")
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
                        log.info("=======listReturn is : {}", JSON.toJSON(listReturn));
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
            //计算奖品归属 如果任意两人平局则结果全部平局
            boolean bl = listBattle.stream().anyMatch(listBattle.get(0).getBean()::equals);
            List<Integer> winner = new ArrayList<>();
            if (bl) {
                log.info("==========平局====================");
                //各自放入背包
                listBattle.stream().forEach(e -> {
                    e.setWin(1);
                    winner.add(e.getUserId());
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
            log.info("saveBoxRecord is {}", JSON.toJSON(listReturn));
            boxrecordservice.saveBoxRecord(listReturn);
            log.info("record 记录列表记录数目:" + gameawardrecords.size());
            gamebattlemapper.saveGameAwardRecords(gameawardrecords);
            dto.setStatus(2);
            log.info("winner is ======" + JSON.toJSONString(winner));
            Object[] s2 = winner.toArray();
            dto.setWinUserId(JSON.toJSONString(s2));
            dto.setDrawCode(CodeUtils.getCode());
            gamebattlemapper.update(dto);
            //修改参与者名单输赢
            winner.stream().forEach(win -> {
                gamebattlemapper.updateUserWin(win, id);
            });
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
                BigDecimal winRate = new BigDecimal(ranking.getWin()).divide(new BigDecimal(ranking.getWin() + ranking.getFail()), 2, BigDecimal.ROUND_HALF_UP);
                ranking.setWinRate(winRate);
                ranking.setExpend(ranking.getExpend().add(dto.getTotalBean()));
                if (ranking.getId() > 0) {
                    //修改
                    gamebattlemapper.updateRanking(ranking);
                } else {
                    gamebattlemapper.saveRanking(ranking);
                }
            });

            //去除监听
            redisTemplate.delete("BATTLE|" + id);
//            websocketservice.sendOneMessage(String.valueOf(id), obj.toJSONString());
            //socket
//            //创建业务消息信息
//            JSONObject obj = new JSONObject();
//            obj.put("status", "start");//开启动画
//            webSocket.sendOneMessage(String.valueOf(id), obj.toJSONString());

            //盲盒对战任务
            listUser.stream().forEach(e -> {

                //socket
                try {
                    log.info("==========ws消息发送==============");
                    //创建业务消息信息
                    JSONObject obj = new JSONObject();
                    obj.put("status", "start");//开启动画
                    webSocket.sendOneMessage(String.valueOf(e.getGameUserId())+String.valueOf(id), obj.toJSONString());
                    log.info("==========ws消息发送结束==============");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                try {
                    String userKey = "UserBlindBox|Day" + e.getGameUserId();
                    //计算失效时间
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String date = sdf.format(new Date());
                    Date date2 = DateUtils.parseDate(date + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.setTime(date2);
                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTime(new Date());
                    long time = calendar1.getTimeInMillis() / 1000 - calendar2.getTimeInMillis() / 1000;
                    Object str = redisTemplate.opsForValue().get(userKey);
                    WelfareRedis red = null;
                    if (!ObjectUtils.isEmpty(str)) {
                        red = JSON.parseObject(str.toString(), WelfareRedis.class);
                        red.setCost(red.getCost() + dto.getTotalBean().intValue());
                    } else {
                        red = new WelfareRedis();
                        red.setCost(dto.getTotalBean().intValue());
                        red.setUserId(e.getGameUserId());
                        red.setList(new ArrayList<>());
                    }
                    redisTemplate.opsForValue().set(userKey, JSON.toJSON(red), time, TimeUnit.SECONDS);
                    log.info("===========充值进度增加=====================");
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                //对战记录
                try {
                    //计算失效时间
                    String userKey = "UserRecharge|DZ" + e.getGameUserId();
                    long time = com.example.demo.util.DateUtils.getTime();
                    Object str = redisTemplate.opsForValue().get(userKey);
                    WelfareRedis red = null;
                    if (!ObjectUtils.isEmpty(str)) {
                        red = JSON.parseObject(str.toString(), WelfareRedis.class);
                        red.setCost(red.getCost() + dto.getTotalBean().intValue());
                    } else {
                        red = new WelfareRedis();
                        red.setCost(dto.getTotalBean().intValue());
                        red.setUserId(e.getGameUserId());
                        red.setList(new ArrayList<>());
                    }
                    redisTemplate.opsForValue().set(userKey, JSON.toJSON(red), time, TimeUnit.SECONDS);
                    log.info("===========对战消费进度更新：本次消费{}=====================", dto.getTotalBean().intValue());
                } catch (Exception exception) {
                    log.error("===========对战消费进度更新失败=====================");
                    exception.printStackTrace();
                }
            });
            return listBattle;
        } else {
            log.info("=================人员加入========={}", listUser.size());
            listUser.stream().forEach(e -> {
                //创建业务消息信息
                JSONObject obj = new JSONObject();
                obj.put("status", "join");//人员加入
                webSocket.sendOneMessage(String.valueOf(e.getGameUserId())+String.valueOf(id), obj.toJSONString());
            });
            //创建业务消息信息
//            JSONObject obj = new JSONObject();
//            obj.put("status", "join");//人员加入
//            webSocket.sendOneMessage(String.valueOf(id), obj.toJSONString());
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
    public GameRankingDto getGameRankingList(GameRankingQuery query) {
        //查询昨日之星
        GameRankingDto dto = gamebattlemapper.queryYestdayStar(query);
        if (ObjectUtils.isEmpty(dto)) {
            dto = new GameRankingDto();
        }
        dto.setRankingList(gamebattlemapper.getGameRankingList(query));
        return dto;
    }

    @Override
    public int socket(int userId) {
        JSONObject obj = new JSONObject();
        obj.put("status", "start");//业务类型
        webSocket.sendOneMessage(String.valueOf(userId), obj.toJSONString());
        return 1;
    }


    @Override
    @Transactional
    public int endBattle(int battleId) {
        //用户退款
        GameArenasDto dto = gamebattlemapper.getGameArenasDetail(battleId);
        if (!ObjectUtils.isEmpty(dto) || dto.getCreateUserId() > 0) {
            //退款
            User user = userservice.getUserById(dto.getCreateUserId());
            BigDecimal balance = user.getBean().add(dto.getTotalBean());
            //扣除金币
            userservice.updateBean(balance, dto.getCreateUserId());
            //清除记录
            gamebattlemapper.deleteBattleById(battleId);
        }
        //释放锁
        redisTemplate.delete("BATTLE|" + battleId + ".lock");
        return 0;
    }

    @Override
    public Object getGameArenasSession(String id) {
        //创建业务消息信息
        JSONObject obj = new JSONObject();
        obj.put("status", "join");//人员加入
        webSocket.sendOneMessage(id, obj.toJSONString());
        log.info("================id:{}创建房间=====================================", id);
        return null;
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
