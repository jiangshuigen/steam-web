package com.example.demo.service.impl;

import com.example.demo.config.Constant;
import com.example.demo.dto.*;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    public int updateUser(User user) {
        return userMapper.updateUser(user);
    }

    public PageInfo<User> getUserListByPage(UserQuery query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<User> list = userMapper.getUserListByPage(query);
        PageInfo<User> userPageInfo = new PageInfo<>(list);
        return userPageInfo;
    }

    public User getUserById(int id) {
        return userMapper.getUserById(id);
    }

    @Override
    public PageInfo<LoginIpLog> getIPList(LoginIpLogQuery query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<LoginIpLog> list = userMapper.getIPList(query);
        PageInfo<LoginIpLog> userPageInfo = new PageInfo<>(list);
        return userPageInfo;
    }

    @Override
    public UserDto getUserInfo(int id) {
        return userMapper.getUserInfo(id);
    }

    @Override
    public int webUpdateUser(UserUpdateDto user) {
        return userMapper.webUpdateUser(user);
    }

    @Override
    public User getLoginUserInfo(HttpServletRequest req) {
        //获取session
        HttpSession session = req.getSession();
        UserDto dto = (UserDto) session.getAttribute(Constant.USER_INFO);
        if (!ObjectUtils.isEmpty(dto)) {
            User user = new User();
            BeanUtils.copyProperties(dto, user);
            return user;
        }
        return null;
    }

    @Override
    public UserDto userLogin(HttpServletRequest request, LoginInfo info) {
        //用户名密码查询（密文）
        UserDto dto = userMapper.queryUserInfo(info);
        if (!ObjectUtils.isEmpty(dto)) {
            //存个Session
            request.getSession().setAttribute(Constant.USER_INFO, dto);
        }
        return dto;
    }


}
