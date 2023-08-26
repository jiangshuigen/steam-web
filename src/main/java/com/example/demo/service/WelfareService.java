package com.example.demo.service;

import com.example.demo.entity.RechangeWelfare;
import com.example.demo.entity.User;

import java.util.List;

public interface WelfareService {
    List<RechangeWelfare> getRechargeWelfare(User usr, int type);

    int getWelfare(User usr, int id, int type);

    int getDays(User usr);
}
