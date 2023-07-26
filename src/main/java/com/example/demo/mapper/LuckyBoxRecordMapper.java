package com.example.demo.mapper;

import com.example.demo.dto.LuckyBoxRecordQuery;
import com.example.demo.entity.LuckyBoxRecord;

import java.util.List;

public interface LuckyBoxRecordMapper {

    List<LuckyBoxRecord> getLuckyBoxList(LuckyBoxRecordQuery query);

    List<LuckyBoxRecord> getHistory(int awardId);

    int saveRecord(LuckyBoxRecord record);
}
