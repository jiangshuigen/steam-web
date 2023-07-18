package com.example.demo.mapper;

import com.example.demo.entity.Vip;

import java.util.List;

public interface VipMapper {

    List<Vip> getVipListByPage();

    Vip getVipById(int id);

    int updateVip(Vip vip);
}
