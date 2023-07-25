package com.example.demo.service;

import com.example.demo.dto.BeanRecordQuery;
import com.example.demo.entity.BeanRecord;
import com.github.pagehelper.PageInfo;

import java.math.BigDecimal;
import java.util.Date;

public interface BeanRecordService {

    PageInfo<BeanRecord> getBeanRecordList(BeanRecordQuery query);

    BigDecimal queryBeanRecords(int userId, Date payStartTime);
}
