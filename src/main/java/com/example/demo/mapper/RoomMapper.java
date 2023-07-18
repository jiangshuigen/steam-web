package com.example.demo.mapper;

import com.example.demo.dto.RoomAwardQuery;
import com.example.demo.dto.RoomQuery;
import com.example.demo.entity.Room;
import com.example.demo.entity.RoomAward;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoomMapper {
    List<Room> getRoomsListByPage(RoomQuery query);

    Room getRoomById(int id);

    int deleteRoom(int id);

    int updateRoom(Room room);

    List<RoomAward> getRoomAwardById(RoomAwardQuery query);

    int deleteRoomIds(@Param("array") List<String> item);
}
