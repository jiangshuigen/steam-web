package com.example.demo.service;

import com.example.demo.dto.BoxAwardsQuery;
import com.example.demo.dto.LuckyBboxRecordQuery;
import com.example.demo.entity.AwardTypes;
import com.example.demo.entity.Box;
import com.example.demo.entity.BoxAwards;
import com.example.demo.entity.LuckyBboxRecord;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface LuckyBoxService {
    List<AwardTypes> getTypeList();

    AwardTypes getTypeById(int id);

    int updateType(AwardTypes type);

    int saveType(AwardTypes type);

    PageInfo<BoxAwards> getAwardList(BoxAwardsQuery query);

    int updateAward(BoxAwards award);

    PageInfo<LuckyBboxRecord> getLuckyBoxList(LuckyBboxRecordQuery query);

    int deleteById(int id);
}
