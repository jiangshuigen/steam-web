package com.example.demo.mapper;

import com.example.demo.entity.PromotionLevels;

import java.util.List;

public interface PromotionLevelMapper {
    List<PromotionLevels> getLevelList();

    PromotionLevels getLevelById(int id);

    int updateLevel(PromotionLevels level);

    int deleteById(int id);
}
