package com.example.demo.service.impl;

import com.example.demo.dto.BoxAwardsQuery;
import com.example.demo.dto.LuckyBboxRecordQuery;
import com.example.demo.entity.AwardTypes;
import com.example.demo.entity.BoxAwards;
import com.example.demo.entity.LuckyBboxRecord;
import com.example.demo.mapper.LuckyBoxMapper;
import com.example.demo.mapper.LuckyBoxRecordMapper;
import com.example.demo.service.LuckyBoxService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LuckyBoxServiceImpl implements LuckyBoxService {

    @Resource
    private LuckyBoxMapper mapper;
    @Resource
    private LuckyBoxRecordMapper luckyboxrecordmapper;

    @Override
    public List<AwardTypes> getTypeList() {
        return mapper.getTypeList();
    }

    @Override
    public AwardTypes getTypeById(int id) {
        return mapper.getTypeById(id);
    }

    @Override
    public int updateType(AwardTypes type) {
        return mapper.updateType(type);
    }

    @Override
    public int saveType(AwardTypes type) {
        return mapper.saveType(type);
    }

    @Override
    public PageInfo<BoxAwards> getAwardList(BoxAwardsQuery query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<BoxAwards> list = mapper.getAwardList(query);
        PageInfo<BoxAwards> listInfo = new PageInfo<>(list);
        return listInfo;
    }

    @Override
    public int updateAward(BoxAwards award) {
        return mapper.updateAward(award);
    }

    @Override
    public PageInfo<LuckyBboxRecord> getLuckyBoxList(LuckyBboxRecordQuery query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<LuckyBboxRecord> list = luckyboxrecordmapper.getLuckyBoxList(query);
        PageInfo<LuckyBboxRecord> listInfo = new PageInfo<>(list);
        return listInfo;
    }

    @Override
    public int deleteById(int id) {
        return mapper.deleteById(id);
    }
}
