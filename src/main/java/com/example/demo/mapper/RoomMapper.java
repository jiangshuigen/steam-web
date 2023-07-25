package com.example.demo.mapper;

import com.example.demo.dto.RoomAwardQuery;
import com.example.demo.dto.RoomAwardsDto;
import com.example.demo.dto.RoomQuery;
import com.example.demo.dto.RoomUserDto;
import com.example.demo.entity.Room;
import com.example.demo.entity.RoomAward;
import com.example.demo.entity.RoomWeb;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoomMapper {
    List<Room> getRoomsListByPage(RoomQuery query);

    Room getRoomById(int id);

    int deleteRoom(int id);

    int updateRoom(Room room);

    List<RoomAward> getRoomAwardById(RoomAwardQuery query);

    int deleteRoomIds(@Param("array") List<String> item);

    List<RoomWeb> getRoomsList();

    List<RoomWeb> getRoomsListByUser(int userId);

    List<RoomAward> getRoomAwardsListById(int roomId);

    List<RoomUserDto> getUsersById(int roomId);

    int saveRoom(Room room);

    int saveBatchAwards(@Param("batch") List<RoomAwardsDto> batch);

    int updateBoxRecord(@Param("ids")List<Integer> batch);

    int updateRoomAwards(int roomId);
}
