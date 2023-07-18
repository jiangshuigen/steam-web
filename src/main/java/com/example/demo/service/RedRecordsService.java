package com.example.demo.service;

import com.example.demo.dto.RedRecordQuery;
import com.example.demo.entity.RedRecords;
import com.github.pagehelper.PageInfo;

public interface RedRecordsService {
    PageInfo<RedRecords> getRecordsListByPage(RedRecordQuery query);
}
