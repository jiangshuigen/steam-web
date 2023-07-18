package com.example.demo.mapper;

import com.example.demo.dto.BasePage;
import com.example.demo.entity.BeanChangeRecords;

import java.util.List;

public interface BeanChangeRecordMapper {
    List<BeanChangeRecords> getBeanRecordList(BasePage query);
}
