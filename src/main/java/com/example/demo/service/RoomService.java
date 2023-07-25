package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.Room;
import com.example.demo.entity.RoomAward;
import com.example.demo.entity.RoomWeb;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface RoomService {

    PageInfo<Room> getRoomsListByPage(RoomQuery query);

    Room getRoomById(int id);

    int deleteRoom(int id);

    int updateRoom(Room room);

    PageInfo<RoomAward> getRoomAwardById(RoomAwardQuery query);

    int deleteRoomList(String ids);

    List<RoomWeb> getRoomsList(WebRoomQuery query);

    List<RoomAward> getRoomAwardsListById(int id);

    List<RoomUserDto> getUsersById(int id);

    int saveRoom(RoomDto room);
}
