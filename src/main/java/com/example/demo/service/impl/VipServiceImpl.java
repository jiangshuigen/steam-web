package com.example.demo.service.impl;

import com.example.demo.dto.BasePage;
import com.example.demo.dto.UserQuery;
import com.example.demo.entity.User;
import com.example.demo.entity.Vip;
import com.example.demo.mapper.UserMapper;
import com.example.demo.mapper.VipMapper;
import com.example.demo.service.UserService;
import com.example.demo.service.VipService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class VipServiceImpl implements VipService {

    @Resource
    private VipMapper vipmapper;

    @Override
    public PageInfo<Vip> getVipListByPage(BasePage page) {
        PageHelper.startPage(page.getPageNo(), page.getPageSize());
        List<Vip> list = vipmapper.getVipListByPage();
        PageInfo<Vip> listInfo = new PageInfo<>(list);
        return listInfo;
    }

    @Override
    public Vip getVipById(int id) {
        return vipmapper.getVipById(id);
    }

    @Override
    public int updateVip(Vip vip) {
        return vipmapper.updateVip(vip);
    }

    @Override
    public Vip getVipBylv(int lv) {
        return vipmapper.getVipBylv(lv);
    }

}
