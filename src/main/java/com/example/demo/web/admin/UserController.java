package com.example.demo.web.admin;

import com.example.demo.config.ResultData;
import com.example.demo.dto.LoginIpLog;
import com.example.demo.dto.LoginIpLogQuery;
import com.example.demo.dto.UserQuery;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/user")
@Api(value = "用户管理", tags = {"用户管理"})
public class UserController {

    @Resource
    private UserService userservice;

    /**
     * 获取用户列表并且分页筛选
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "用户列表")
    @PostMapping("/getUsers")
    public ResultData<PageInfo<User>> getUserList(@RequestBody UserQuery query) {
        return ResultData.success(userservice.getUserListByPage(query));
    }


    /**
     * 用户信息修改
     *
     * @param
     * @return
     */
    @ApiOperation(value = "用户信息修改")
    @PostMapping("/updateUser")
    public ResultData updateUser(@RequestBody User user) {
        return ResultData.success(userservice.updateUser(user));
    }

    /**
     * 主键查询
     *
     * @param
     * @return
     */
    @ApiOperation(value = "主键查询User")
    @GetMapping("/getUserById")
    public ResultData<User> getUserById(@RequestParam("id") int id) {
        return ResultData.success(userservice.getUserById(id));
    }


    /**
     * 登录信息
     *
     * @param
     * @return
     */
    @ApiOperation(value = "登录IP信息")
    @PostMapping("/getIPs")
    public ResultData<PageInfo<LoginIpLog>> getIPList(@RequestBody LoginIpLogQuery query) {
        return ResultData.success(userservice.getIPList(query));
    }

    /**
     * 清理缓存
     * @param
     * @return
     */
    @ApiOperation(value = "清理缓存")
    @PostMapping("/resetCache")
    public ResultData<PageInfo<LoginIpLog>> resetCache() {
        return ResultData.success(userservice.resetCache());
    }
}
