package com.example.demo.service;

import com.example.demo.dto.BasePage;
import com.example.demo.entity.BeanChangeRecords;
import com.github.pagehelper.PageInfo;

public interface BeanChangeRecordService {

    PageInfo<BeanChangeRecords> getBeanChangeRecordList(BasePage query);
}
