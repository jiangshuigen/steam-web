package com.example.demo.service.impl;

import com.example.demo.entity.Dashboard;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.DashboardService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Resource
    private UserMapper userMapper;

    @Override
    public Dashboard getDashboard() {
        return userMapper.getDashboard();
    }
}
