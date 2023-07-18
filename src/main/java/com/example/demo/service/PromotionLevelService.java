package com.example.demo.service;

import com.example.demo.entity.PromotionLevels;

import java.util.List;

public interface PromotionLevelService {
    List<PromotionLevels> getLevelList();

    PromotionLevels getLevelById(int id);

    int updateLevel(PromotionLevels level);

    int deleteLevel(int id);
}
