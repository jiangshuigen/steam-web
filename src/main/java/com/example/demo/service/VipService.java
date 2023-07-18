package com.example.demo.service;

import com.example.demo.dto.BasePage;
import com.example.demo.entity.Vip;
import com.github.pagehelper.PageInfo;

public interface VipService {

    PageInfo<Vip> getVipListByPage(BasePage page);

    Vip getVipById(int id);

    int updateVip(Vip vip);
}
