package com.example.demo.mapper;

import com.example.demo.dto.LuckyBboxRecordQuery;
import com.example.demo.entity.LuckyBboxRecord;

import java.util.List;

public interface LuckyBoxRecordMapper {

    List<LuckyBboxRecord> getLuckyBoxList(LuckyBboxRecordQuery query);
}
