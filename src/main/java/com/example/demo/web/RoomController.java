package com.example.demo.web;

import com.example.demo.config.ResultData;
import com.example.demo.dto.RoomAwardQuery;
import com.example.demo.dto.RoomQuery;
import com.example.demo.entity.Room;
import com.example.demo.entity.RoomAward;
import com.example.demo.service.RoomService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/admin/room")
@Api(value = "房间管理", tags = {"房间管理"})
public class RoomController {

    @Resource
    private RoomService roomservice;

    /**
     * 获取房间列表
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "获取房间列表")
    @PostMapping("/getRoomsList")
    public ResultData<PageInfo<Room>> getRoomsListByPage(@RequestBody RoomQuery query) {
        return ResultData.success(roomservice.getRoomsListByPage(query));
    }

    /**
     * 修改
     *
     * @param room
     * @return
     */
    @ApiOperation(value = "修改房间")
    @PostMapping("/updateRoom")
    public ResultData updateRoom(@RequestBody Room room) {
        return ResultData.success(roomservice.updateRoom(room));
    }

    /**
     * 主键查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "主键查询")
    @GetMapping("/getRoomById")
    public ResultData<Room> getRoomById(@RequestParam int id) {
        return ResultData.success(roomservice.getRoomById(id));
    }

    /**
     * 获取房间装备
     *
     * @param
     * @return
     */
    @ApiOperation(value = "房间装备")
    @PostMapping("/getRoomAwardById")
    public ResultData<PageInfo<RoomAward>> getRoomAwardById(@RequestBody RoomAwardQuery query) {
        return ResultData.success(roomservice.getRoomAwardById(query));
    }


    /**
     * 删除
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除")
    @DeleteMapping("/delete")
    public ResultData deleteRoom(@RequestParam int id) {
        return ResultData.success(roomservice.deleteRoom(id));
    }

    /**
     * 批量删除
     *
     * @param
     * @return
     */
    @ApiOperation(value = "批量删除")
    @DeleteMapping("/deleteList")
    public ResultData deleteRoomList(@RequestParam("Ids") String ids) {
        return ResultData.success(roomservice.deleteRoomList(ids));
    }

}
