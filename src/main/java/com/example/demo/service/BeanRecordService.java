package com.example.demo.service;

import com.example.demo.dto.BeanRecordQuery;
import com.example.demo.entity.BeanRecord;
import com.github.pagehelper.PageInfo;

public interface BeanRecordService {

    PageInfo<BeanRecord> getBeanRecordList(BeanRecordQuery query);

}
