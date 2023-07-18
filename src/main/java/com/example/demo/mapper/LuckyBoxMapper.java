package com.example.demo.mapper;

import com.example.demo.dto.BoxAwardsQuery;
import com.example.demo.dto.LuckyBboxRecordQuery;
import com.example.demo.entity.AwardTypes;
import com.example.demo.entity.BoxAwards;
import com.example.demo.entity.LuckyBboxRecord;

import java.util.List;

public interface LuckyBoxMapper {

    List<AwardTypes> getTypeList();

    AwardTypes getTypeById(int id);

    int updateType(AwardTypes type);

    int saveType(AwardTypes type);

    List<BoxAwards> getAwardList(BoxAwardsQuery query);

    int updateAward(BoxAwards award);

    int deleteById(int id);
}
