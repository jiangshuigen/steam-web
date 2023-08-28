package com.example.demo.service;

import com.example.demo.dto.VipReturnDto;
import com.example.demo.entity.RechangeWelfare;
import com.example.demo.entity.User;

import java.math.BigDecimal;
import java.util.List;

public interface WelfareService {
    List<RechangeWelfare> getRechargeWelfare(User usr, int type);

    int getWelfare(User usr, int id, int type);

    int getDays(User usr);

    VipReturnDto getVipList(User usr);

    BigDecimal getVipWelfare(User usr, int lv);
}
