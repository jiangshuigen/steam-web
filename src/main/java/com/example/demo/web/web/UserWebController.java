package com.example.demo.web.web;

import com.example.demo.config.ResultData;
import com.example.demo.dto.BoxRecordsWebQuery;
import com.example.demo.dto.LoginInfo;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.UserUpdateDto;
import com.example.demo.entity.BoxRecordsWeb;
import com.example.demo.entity.User;
import com.example.demo.service.BoxRecordService;
import com.example.demo.service.UserService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/web")
@Api(value = "用户中心(前台)", tags = {"用户中心(前台)"})
public class UserWebController {
    @Resource
    private UserService userservice;
    @Resource
    private BoxRecordService boxrecordservice;


    /**
     * 用户登录
     *
     * @param info
     * @return
     */
    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public ResultData login(HttpServletRequest request, @RequestBody LoginInfo info) {
        UserDto dto = userservice.userLogin(request, info);
        if (!ObjectUtils.isEmpty(dto)) {
            return ResultData.success(dto);
        } else {
            return ResultData.fail("403", "用户名密码错误");
        }

    }


    /**
     * 用户中心
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "主键查询User")
    @GetMapping("/getUserById")
    public ResultData<UserDto> getUserInfo(@RequestParam("id") int id) {
        return ResultData.success(userservice.getUserInfo(id));
    }


    /**
     * 修改
     *
     * @param user
     * @return
     */
    @ApiOperation(value = "用户信息修改")
    @PostMapping("/updateUser")
    public ResultData updateUser(@RequestBody UserUpdateDto user) {
        return ResultData.success(userservice.webUpdateUser(user));
    }


    /**
     * 我的背包
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "我的背包")
    @PostMapping("/getList")
    public ResultData<PageInfo<BoxRecordsWeb>> getMyPackage(HttpServletRequest request, @RequestBody BoxRecordsWebQuery query) {
        //获取用户信息
        User usr = userservice.getLoginUserInfo(request);
        if (!ObjectUtils.isEmpty(usr)) {
            query.setUserId(usr.getId());
            return ResultData.success(boxrecordservice.getMyPackage(query));
        } else {
            return ResultData.fail("403", "请登录");
        }
    }
}
