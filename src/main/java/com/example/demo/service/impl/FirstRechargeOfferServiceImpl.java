package com.example.demo.service.impl;

import com.example.demo.entity.FirstRechargeOffer;
import com.example.demo.mapper.FirstRechargeOfferMapper;
import com.example.demo.service.FirstRechargeOfferService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FirstRechargeOfferServiceImpl implements FirstRechargeOfferService {

    @Resource
    private FirstRechargeOfferMapper firstrechargeoffermapper;

    @Override
    public List<FirstRechargeOffer> getFirstRechargeList() {
        return firstrechargeoffermapper.getFirstRechargeList();
    }

    @Override
    public FirstRechargeOffer getFirstRechargeById(int id) {
        return firstrechargeoffermapper.getFirstRechargeById(id);
    }

    @Override
    public int updateFirstRecharge(FirstRechargeOffer firstrecharge) {
        return firstrechargeoffermapper.updateFirstRecharge(firstrecharge);
    }

    @Override
    public int deleteFirstRecharge(int id) {
        return firstrechargeoffermapper.deleteFirstRecharge(id);
    }

    @Override
    public int saveFirstRecharge(FirstRechargeOffer firstrecharge) {
        return firstrechargeoffermapper.saveFirstRecharge(firstrecharge);
    }
}
