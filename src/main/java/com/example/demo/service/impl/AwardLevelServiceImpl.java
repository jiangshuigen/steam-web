package com.example.demo.service.impl;

import com.example.demo.entity.AwardLevels;
import com.example.demo.mapper.AwardLevelMapper;
import com.example.demo.service.AwardLevelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AwardLevelServiceImpl implements AwardLevelService {

    @Resource
    private AwardLevelMapper mapper;

    @Override
    public List<AwardLevels> getAwardLevelList() {
        return mapper.getAwardLevelList();
    }

    @Override
    public AwardLevels getLevelById(int id) {
        return mapper.getLevelById(id);
    }

    @Override
    public int updateLevel(AwardLevels level) {
        return mapper.updateLevel(level);
    }
}
