package com.example.demo.mapper;

import com.example.demo.entity.FirstRechargeOffer;

import java.util.List;

public interface FirstRechargeOfferMapper {
    List<FirstRechargeOffer> getFirstRechargeList();

    FirstRechargeOffer getFirstRechargeById(int id);

    int updateFirstRecharge(FirstRechargeOffer firstrecharge);

    int deleteFirstRecharge(int id);

    int saveFirstRecharge(FirstRechargeOffer firstrecharge);
}
