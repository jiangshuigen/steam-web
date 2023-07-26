package com.example.demo.web.web;

import com.example.demo.config.ResultData;
import com.example.demo.dto.JoinRoomDto;
import com.example.demo.dto.RoomDto;
import com.example.demo.dto.RoomUserDto;
import com.example.demo.dto.WebRoomQuery;
import com.example.demo.entity.RoomAward;
import com.example.demo.entity.RoomWeb;
import com.example.demo.entity.User;
import com.example.demo.service.RoomService;
import com.example.demo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/webRoom")
@Api(value = "ROLL房(前台)", tags = {"ROLL房(前台)"})
@Slf4j
public class WebRoomController {
    @Resource
    private RoomService roomservice;
    @Resource
    private UserService userservice;

    /**
     * 获取房间列表
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "获取房间列表")
    @PostMapping("/getRoomsList")
    public ResultData<List<RoomWeb>> getRoomsListByPage(HttpServletRequest request, @RequestBody WebRoomQuery query) {
        //验证登录
        User usr = userservice.getLoginUserInfo(request);
        if (query.getUserId() > 0 && ObjectUtils.isEmpty(usr)) {
            return ResultData.fail("403", "请登录");
        }
        return ResultData.success(roomservice.getRoomsList(query));
    }

    /**
     * 房间奖品查询
     *
     * @param
     * @return
     */
    @ApiOperation(value = "奖池奖品查询")
    @GetMapping("/getRoomAwardById")
    public ResultData<List<RoomAward>> getRoomAwardById(@RequestParam int id) {
        return ResultData.success(roomservice.getRoomAwardsListById(id));
    }


    /**
     * 参与用户查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "参与用户查询")
    @GetMapping("/getUsersById")
    public ResultData<List<RoomUserDto>> getUsersById(@RequestParam int id) {
        return ResultData.success(roomservice.getUsersById(id));
    }

    /**
     * 创建房间
     *
     * @param room
     * @return
     */
    @ApiOperation(value = "创建房间")
    @PostMapping("/saveRoom")
    public ResultData updateRoom(HttpServletRequest request, @RequestBody RoomDto room) {
        try {
            //验证登录
            User usr = userservice.getLoginUserInfo(request);
            if (ObjectUtils.isEmpty(usr)) {
                return ResultData.fail("403", "请登录");
            }
            room.setUserId(usr.getId());
            return ResultData.success(roomservice.saveRoom(room));
        } catch (Exception e) {
            e.printStackTrace();
            log.info("创建房间异常=======" + e.getMessage());
            return ResultData.fail("500", e.getMessage());
        }
    }


    /**
     * 加入房间
     *
     * @param request
     * @param
     * @return
     */
    @ApiOperation(value = "加入房间")
    @PostMapping("/joinRoom")
    public ResultData joinRoom(HttpServletRequest request, @RequestBody JoinRoomDto dto) {
        try {
            //验证登录
            User usr = userservice.getLoginUserInfo(request);
            if (ObjectUtils.isEmpty(usr)) {
                return ResultData.fail("403", "请登录");
            }
            dto.setUserId(usr.getId());
            return ResultData.success(roomservice.joinRoom(dto, usr.getInviterId()));
        } catch (Exception e) {
            e.printStackTrace();
            log.info("加入房间异常=======" + e.getMessage());
            return ResultData.fail("500", e.getMessage());
        }
    }
}
