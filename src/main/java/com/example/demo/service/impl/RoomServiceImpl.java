package com.example.demo.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.example.demo.dto.*;
import com.example.demo.entity.Room;
import com.example.demo.entity.RoomAward;
import com.example.demo.entity.RoomWeb;
import com.example.demo.mapper.RoomMapper;
import com.example.demo.service.BeanRecordService;
import com.example.demo.service.RoomService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoomServiceImpl implements RoomService {

    @Resource
    private RoomMapper roomMapper;
    @Resource
    private BeanRecordService beanrecordservice;
    @Autowired
    private RedisTemplate redisTemplate;

    Integer i = 0;

    @Override
    public PageInfo<Room> getRoomsListByPage(RoomQuery query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<Room> list = roomMapper.getRoomsListByPage(query);
        for (Room room : list) {
            //查询房间玩家
            List<RoomUserDto> listUser = roomMapper.getUsersById(room.getId());
            room.setListUser(listUser);
        }
        PageInfo<Room> listInfo = new PageInfo<>(list);
        return listInfo;
    }

    @Override
    public Room getRoomById(int id) {
        return roomMapper.getRoomById(id);
    }

    @Override
    @Transactional
    public int deleteRoom(int id) {
        //未开始的房间数据回复
        roomMapper.updateRoomAwards(id);
        return roomMapper.deleteRoom(id);
    }

    @Override
    public int updateRoom(Room room) {
        return roomMapper.updateRoom(room);
    }

    @Override
    public PageInfo<RoomAward> getRoomAwardById(RoomAwardQuery query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<RoomAward> list = roomMapper.getRoomAwardById(query);
        PageInfo<RoomAward> listInfo = new PageInfo<>(list);
        return listInfo;
    }

    @Override
    @Transactional
    public int deleteRoomList(String ids) {
        List<String> list = Arrays.stream(ids.split(",")).collect(Collectors.toList());
        list.stream().forEach(e -> {
            roomMapper.updateRoomAwards(Integer.parseInt(e));
        });
        return roomMapper.deleteRoomIds(list);
    }

    @Override
    public List<RoomWeb> getRoomsList(WebRoomQuery query) {
        if (!ObjectUtils.isEmpty(query) && query.getUserId() > 0) {
            List<RoomWeb> list = roomMapper.getRoomsListByUser(query.getUserId());
            for (RoomWeb roomWeb : list) {
                log.info("======查询房间号为：{},房间名称{}的用户列表", roomWeb.getId(), roomWeb.getName());
                List<RoomUserDto> listUser = roomMapper.getUsersById(roomWeb.getId());
                roomWeb.setListUser(listUser);
            }
            return list;
        } else {
            List<RoomWeb> list = roomMapper.getRoomsList();
            for (RoomWeb roomWeb : list) {
                log.info("======查询房间号为：{},房间名称{}的用户列表", roomWeb.getId(), roomWeb.getName());
                List<RoomUserDto> listUser = roomMapper.getUsersById(roomWeb.getId());
                roomWeb.setListUser(listUser);
            }
            List<RoomWeb> retrunList = null;
            if (!ObjectUtils.isEmpty(query) && query.getStatus() > 0) {
                retrunList = list.stream().filter(e -> e.getStatus() == query.getStatus()).collect(Collectors.toList());
                return this.calculate(retrunList);
            }
            return this.calculate(list);
        }
    }

    @Override
    public List<RoomAward> getRoomAwardsListById(int id) {
        return roomMapper.getRoomAwardsListById(id);
    }

    @Override
    public List<RoomUserDto> getUsersById(int roomId) {
        return roomMapper.getUsersById(roomId);
    }

    @Override
    @Transactional
    public int saveRoom(RoomDto roomdto) throws Exception {
        log.info("======param:" + JSON.toJSONString(roomdto));
        Room room = new Room();
        BeanUtils.copyProperties(roomdto, room);
        int i = roomMapper.saveRoom(room);
        if (i > 0) {
            List<RoomAwardsDto> batch = new ArrayList<>();
            roomdto.getAwards().stream().forEach(e -> {
                RoomAwardsDto dto = new RoomAwardsDto();
                dto.setRoomId(room.getId());
                dto.setBoxRecordId(e);
                batch.add(dto);
            });
            i = roomMapper.saveBatchAwards(batch);
            log.info("======添加奖品==size:" + i);
            i = roomMapper.updateBoxRecord(roomdto.getAwards());
            log.info("======扣除背包的===size:" + i);
            log.info("======添加监听key===" + "ROOM|" + room.getId());
            long tim = (room.getEndTime().getTime() - new Date().getTime()) / 1000;
            //设置>2小时
            long two = 2 * 60 * 60;
            if (tim >= two) {
                redisTemplate.opsForValue().set("ROOM|" + room.getId(), room.getId(), tim, TimeUnit.SECONDS);
            } else {
                throw new Exception("开奖时间最少在当前时间基础上加2小时");
            }
        }
        return i;
    }

    @Override
    public int joinRoom(JoinRoomDto dto, int inviterId) throws Exception {
        Room room = roomMapper.getRoomById(dto.getRoomId());
        if (!ObjectUtils.isEmpty(room)) {
            //邀请校验
            if (room.getMeInviter() == 1) {
                if (room.getUserId() != inviterId) {
                    throw new Exception("仅限个人推广下级用户参与本房间");
                }
            }
            //密码校验
            if (!StringUtils.isEmpty(room.getPassword()) && !StringUtils.equals(room.getPassword(), dto.getPassword())) {
                throw new Exception("密码错误");
            }
            //人数校验
            List<RoomUserDto> listUser = roomMapper.getUsersById(dto.getRoomId());
            if (listUser.size() >= room.getPeopleNumber()) {
                throw new Exception("无法加入，人数已经达到上限");
            }
            //查重校验
            for (RoomUserDto roomUserDto : listUser) {
                if (roomUserDto.getUserId() == dto.getUserId()) {
                    throw new Exception("您已经加入该房间");
                }
            }
            //充值校验
            if (room.getMinRecharge().compareTo(new BigDecimal(0)) == 1) {
                BigDecimal numb = beanrecordservice.queryBeanRecords(dto.getUserId(), room.getPayStartTime());
                if (room.getMinRecharge().compareTo(numb) == 1) {
                    throw new Exception("无法加入,房间要求自" + room.getPayStartTime() + "开始总充值金额达到 " + room.getMinRecharge() + "R币 才可进入！");
                }
            }
            //加入房间
            roomMapper.insertRoomUser(dto);
        } else {
            throw new Exception("房间不存在或者已经下线");
        }
        return 0;
    }

    @Override
    @Transactional
    public int endRoom(int roomId) {
        log.info("========房间" + roomId + "开奖=============");
        Room room = roomMapper.getRoomById(roomId);
        if (room.getIsGive() == 1) {
            log.info("========房间" + roomId + "已经结束=============");
        }
        List<RoomAward> awardList = roomMapper.getRoomAwardsListById(roomId);
//        //指定用户奖品处理
//        List<RoomAward> awardListDesign = awardList.stream().filter(e -> e.getDesignatedUser() > 0).collect(Collectors.toList());
        List<RoomUserDto> listUser = roomMapper.getUsersById(roomId);
        List<RoomAwardDto> listRoomAwardDto = new ArrayList<>();
        awardList.stream().forEach(e -> {
            RoomAwardDto dto = new RoomAwardDto();
            dto.setId(e.getId());
            dto.setBoxRecordId(e.getBoxRecordId());
            //指定用户
            if (e.getDesignatedUser() > 0) {
                dto.setGetUserId(e.getDesignatedUser());
            } else {
                //随机抽取用户
                if (listUser.size() > 0) {
                    int index = new Random().nextInt(listUser.size());
                    RoomUserDto userDto = listUser.get(index);
                    dto.setGetUserId(userDto.getUserId());
                }
            }
            listRoomAwardDto.add(dto);
        });
        int x = roomMapper.updateBatchAwards(listRoomAwardDto);
        log.info("=====发放奖品数目：" + x);
        int num = roomMapper.updateBatchPackage(listRoomAwardDto);
        log.info("=====背包放入数目：" + num);
        //活动结束
        roomMapper.updateRoomGive(roomId);
        //释放锁
        redisTemplate.delete("ROOM|" + roomId + ".lock");
        return 0;
    }


    private List<RoomWeb> calculate(List<RoomWeb> list) {
        list.stream().forEach(el -> {
            BigDecimal num = el.getAwardList().stream().map(e -> {
                        if (e.getRoomAwardBean() == null) {
                            return BigDecimal.ZERO;
                        } else {
                            return e.getRoomAwardBean();
                        }
                    }
            ).reduce(BigDecimal.ZERO, BigDecimal::add);
            el.setAwardsPool(num);
        });
        return list;
    }
}
