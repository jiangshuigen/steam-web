package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.User;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

public interface UserService {

    PageInfo<User> getUserListByPage(UserQuery query);

    int updateUser(User user);

    User getUserById(int id);

    PageInfo<LoginIpLog> getIPList(LoginIpLogQuery query);


    UserDto getUserInfo(int id);

    int webUpdateUser(UserUpdateDto user);

    /**
     * 获取登录用户信息
     * @return
     */
    User getLoginUserInfo(HttpServletRequest req);

    UserDto userLogin(HttpServletRequest request, LoginInfo info);

    String register(HttpServletRequest request,UserRegisterDto user);

    String sendCode(String phone);

    boolean repeatCheck(String str,int type);

    int updateBean(BigDecimal balance, int id);
}