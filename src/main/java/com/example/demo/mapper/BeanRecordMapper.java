package com.example.demo.mapper;

import com.example.demo.dto.BeanRecordQuery;
import com.example.demo.entity.BeanRecord;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface BeanRecordMapper {
    List<BeanRecord> getBeanRecordList(BeanRecordQuery query);

    BigDecimal queryBeanRecords(int userId, Date payStartTime);
}
