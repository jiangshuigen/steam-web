package com.example.demo.service.impl;

import com.example.demo.dto.LoginIpLog;
import com.example.demo.dto.LoginIpLogQuery;
import com.example.demo.dto.UserQuery;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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


}
