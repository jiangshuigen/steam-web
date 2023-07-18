package com.example.demo.service.impl;

import com.example.demo.dto.RoomAwardQuery;
import com.example.demo.dto.RoomQuery;
import com.example.demo.entity.Room;
import com.example.demo.entity.RoomAward;
import com.example.demo.mapper.RoomMapper;
import com.example.demo.service.RoomService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    @Resource
    private RoomMapper roomMapper;

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
    public int deleteRoom(int id) {
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
        return roomMapper.deleteRoomIds(list);
    }
}
