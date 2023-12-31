package com.example.demo.web.web;

import com.example.demo.config.ResultData;
import com.example.demo.dto.VipReturnDto;
import com.example.demo.entity.RechangeWelfare;
import com.example.demo.entity.RoomWeb;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.service.WelfareService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/web/welfare")
@Api(value = "福利中心", tags = {"福利中心"})
@Slf4j
public class WelfareController {
    @Resource
    private UserService userservice;
    @Resource
    private WelfareService welfareservice;

    /**
     * 充值福利
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "福利:1-充值福利 2-每月累充 3-盲盒任务 4-对战任务")
    @GetMapping("/getRechargeWelfare")
    public ResultData<List<RechangeWelfare>> getRechargeWelfare(HttpServletRequest request, @RequestParam int type) {
        //验证登录
        User usr = userservice.getLoginUserInfo(request);
        if (ObjectUtils.isEmpty(usr)) {
            return ResultData.fail("403", "请登录");
        }
        return ResultData.success(welfareservice.getRechargeWelfare(usr, type));
    }


    /**
     * 福利
     *
     * @param request
     * @param
     * @return
     */
    @ApiOperation(value = "领取")
    @PostMapping("/getWelfare")
    public ResultData<List<RoomWeb>> getWelfare(HttpServletRequest request, @RequestParam int id, @RequestParam int type) {
        //验证登录
        User usr = userservice.getLoginUserInfo(request);
        if (ObjectUtils.isEmpty(usr)) {
            return ResultData.fail("403", "请登录");
        }
        User us = userservice.getUserById(usr.getId());
        return ResultData.success(welfareservice.getWelfare(us, id, type));
    }


    /**
     * 当日获取
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "当日获得")
    @PostMapping("/getDays")
    public ResultData<List<RoomWeb>> getDays(HttpServletRequest request) {
        //验证登录
        User usr = userservice.getLoginUserInfo(request);
        if (ObjectUtils.isEmpty(usr)) {
            return ResultData.fail("403", "请登录");
        }
        return ResultData.success(welfareservice.getDays(usr));
    }


    /**
     * 查询会员等级奖励
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "获取vip列表")
    @PostMapping("/getVipList")
    public ResultData<VipReturnDto> getVipList(HttpServletRequest request) {
        //验证登录
        User usr = userservice.getLoginUserInfo(request);
        if (ObjectUtils.isEmpty(usr)) {
            return ResultData.fail("403", "请登录");
        }
        return ResultData.success(welfareservice.getVipList(usr));
    }


    @ApiOperation(value = "领取Vip奖励")
    @PostMapping("/getVipWelfare")
    public ResultData<List<RoomWeb>> getVipWelfare(HttpServletRequest request, @RequestParam int lv) {
        //验证登录
        User usr = userservice.getLoginUserInfo(request);
        if (ObjectUtils.isEmpty(usr)) {
            return ResultData.fail("403", "请登录");
        }
        if (lv > usr.getVipLevel()) {
            return ResultData.fail("403", "非法参数");
        }
        User us = userservice.getUserById(usr.getId());
        return ResultData.success(welfareservice.getVipWelfare(us, lv));
    }

}
