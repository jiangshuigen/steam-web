package com.example.demo.mapper;

import com.example.demo.entity.AwardLevels;

import java.util.List;

public interface AwardLevelMapper {
    List<AwardLevels> getAwardLevelList();

    AwardLevels getLevelById(int id);

    int updateLevel(AwardLevels level);
}
