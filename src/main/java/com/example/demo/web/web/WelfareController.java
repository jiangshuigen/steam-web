package com.example.demo.web.web;

import com.example.demo.config.ResultData;
import com.example.demo.dto.WebRoomQuery;
import com.example.demo.entity.RoomWeb;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;

@RestController
@RequestMapping("/web/welfare")
@Api(value = "福利中心", tags = {"福利中心"})
@Slf4j
public class WelfareController {
    @Resource
    private UserService userservice;


    /**
     * 充值福利
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "充值福利")
    @GetMapping("/getRechargeWelfare")
    public ResultData<List<RoomWeb>> getRoomsListByPage(HttpServletRequest request) {
        //验证登录
        User usr = userservice.getLoginUserInfo(request);
        if (ObjectUtils.isEmpty(usr)) {
            return ResultData.fail("403", "请登录");
        }
        try {
            File file = ResourceUtils.getFile("classpath:welfare/welfare.json");
            String jsStr = JsonUtil.getJsonStr(file);
            System.out.println(jsStr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return null;
    }
}
