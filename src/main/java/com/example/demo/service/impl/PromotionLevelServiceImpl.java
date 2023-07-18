package com.example.demo.service.impl;

import com.example.demo.entity.PromotionLevels;
import com.example.demo.mapper.PromotionLevelMapper;
import com.example.demo.service.PromotionLevelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PromotionLevelServiceImpl implements PromotionLevelService {

    @Resource
    private PromotionLevelMapper promotionlevelmapper;

    @Override
    public List<PromotionLevels> getLevelList() {
        return promotionlevelmapper.getLevelList();
    }

    @Override
    public PromotionLevels getLevelById(int id) {
        return promotionlevelmapper.getLevelById(id);
    }

    @Override
    public int updateLevel(PromotionLevels level) {
        return promotionlevelmapper.updateLevel(level);
    }

    @Override
    public int deleteLevel(int id) {
        return promotionlevelmapper.deleteById(id);
    }
}
