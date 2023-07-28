package com.example.demo.service;

import com.example.demo.dto.PromotionInfo;
import com.example.demo.dto.PromotionQuery;
import com.example.demo.dto.PromotionUser;
import com.example.demo.entity.PromotionLevels;

import java.util.List;

public interface PromotionLevelService {
    List<PromotionLevels> getLevelList();

    PromotionLevels getLevelById(int id);

    int updateLevel(PromotionLevels level);

    int deleteLevel(int id);

    PromotionInfo getPromotionInfo(int id);

    List<PromotionUser> getPromotionList(PromotionQuery query);
}
