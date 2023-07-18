package com.example.demo.service;

import com.example.demo.entity.AwardLevels;

import java.util.List;

public interface AwardLevelService {

    List<AwardLevels> getAwardLevelList();

    AwardLevels getLevelById(int id);

    int updateLevel(AwardLevels level);
}
