package com.example.demo.mapper;

import com.example.demo.dto.RedRecordQuery;
import com.example.demo.entity.RedRecords;

import java.util.List;

public interface RedRecordsMapper {
    List<RedRecords> getRecordsListByPage(RedRecordQuery query);
}
