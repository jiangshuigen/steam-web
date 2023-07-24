package com.example.demo.web.web;


import com.example.demo.config.Constant;
import com.example.demo.config.ResultData;
import com.example.demo.dto.BasePage;
import com.example.demo.dto.GameArenasDto;
import com.example.demo.dto.GameArenasSaveDto;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.Box;
import com.example.demo.service.BoxService;
import com.example.demo.service.GameBattleService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/battle")
@Api(value = "盲盒对战(前台)", tags = {"盲盒对战(前台)"})
public class GameBattleController {


    @Resource
    private GameBattleService gamebattleservice;

    @Resource
    private BoxService boxservice;

    /**
     * 查询活动列表
     *
     * @param base
     * @return
     */
    @ApiOperation(value = "活动列表")
    @PostMapping("/getList")
    public ResultData<PageInfo<GameArenasDto>> getEventList(@RequestBody BasePage base) {
        return ResultData.success(gamebattleservice.getEventList(base));
    }

    /**
     * 活动查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "活动人员和回合查询")
    @GetMapping("/getGameArenasDetail")
    public ResultData<GameArenasDto> getGameArenasDetail(@RequestParam("id") int id) {
        return ResultData.success(gamebattleservice.getGameArenasDetail(id));
    }

    /**
     * 查询可选择宝箱列表
     *
     * @return
     */
    @ApiOperation(value = "查询可选择宝箱列表")
    @GetMapping("/getBoxList")
    public ResultData<PageInfo<Box>> getBoxList() {
        return ResultData.success(boxservice.getGameArenaBoxList());
    }

    /**
     * 创建活动
     *
     * @param request
     * @param info
     * @return
     */
    @ApiOperation(value = "创建活动")
    @PostMapping("/createEvent")
    public ResultData createEvent(HttpServletRequest request, @RequestBody GameArenasSaveDto info) {
        //获取session
        HttpSession session = request.getSession();
        UserDto dto = (UserDto) session.getAttribute(Constant.USER_INFO);
        if (ObjectUtils.isEmpty(dto)) {
            return ResultData.fail("403", "未登录");
        }
        info.setCreateUserId(dto.getId());
        return ResultData.success(gamebattleservice.createEvent(info));
    }


    /**
     * 加入
     *
     * @param request
     * @param id
     * @return
     */
    @ApiOperation(value = "加入活动")
    @PostMapping("/join")
    public ResultData joinEvent(HttpServletRequest request, @RequestParam("id") int id) {
        try {
            //获取session
            HttpSession session = request.getSession();
            UserDto dto = (UserDto) session.getAttribute(Constant.USER_INFO);
            if (ObjectUtils.isEmpty(dto)) {
                return ResultData.fail("403", "未登录");
            }
            return ResultData.success(gamebattleservice.joinEvent(id, dto));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.fail("500", e.getMessage());
        }

    }

}
