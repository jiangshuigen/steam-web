package com.example.demo.mapper;

import com.example.demo.dto.BeanRecordQuery;
import com.example.demo.entity.BeanRecord;

import java.util.List;

public interface BeanRecordMapper {
    List<BeanRecord> getBeanRecordList(BeanRecordQuery query);
}
