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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoomServiceImpl implements RoomService {

    @Resource
    private RoomMapper roomMapper;
    @Resource
    private BeanRecordService beanrecordservice;

    Integer i = 0;

    @Override
    public PageInfo<Room> getRoomsListByPage(RoomQuery query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<Room> list = roomMapper.getRoomsListByPage(query);
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
            return list;
        } else {
            List<RoomWeb> list = roomMapper.getRoomsList();
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
    public int saveRoom(RoomDto roomdto) {
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
        }
        return i;
    }

    @Override
    public int joinRoom(JoinRoomDto dto) throws Exception {
        Room room = roomMapper.getRoomById(dto.getRoomId());
        if (!ObjectUtils.isEmpty(room)) {
            //密码校验
            if (!StringUtils.isEmpty(room.getPassword()) && StringUtils.equals(room.getPassword(), dto.getPassword())) {
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
