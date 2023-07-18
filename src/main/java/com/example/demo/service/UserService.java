package com.example.demo.service;

import com.example.demo.dto.LoginIpLog;
import com.example.demo.dto.LoginIpLogQuery;
import com.example.demo.dto.UserQuery;
import com.example.demo.entity.User;
import com.github.pagehelper.PageInfo;

public interface UserService {

    PageInfo<User> getUserListByPage(UserQuery query);

    int updateUser(User user);

    User getUserById(int id);

    PageInfo<LoginIpLog> getIPList(LoginIpLogQuery query);


}
