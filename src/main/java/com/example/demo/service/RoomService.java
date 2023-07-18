package com.example.demo.service;

import com.example.demo.dto.RoomAwardQuery;
import com.example.demo.dto.RoomQuery;
import com.example.demo.entity.Room;
import com.example.demo.entity.RoomAward;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface RoomService {

    PageInfo<Room> getRoomsListByPage(RoomQuery query);

    Room getRoomById(int id);

    int deleteRoom(int id);

    int updateRoom(Room room);

    PageInfo<RoomAward> getRoomAwardById(RoomAwardQuery query);

    int deleteRoomList(String ids);
}
