package com.example.demo.mapper;

import com.example.demo.dto.BeanRecordQuery;
import com.example.demo.entity.BeanRecord;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface BeanRecordMapper {
    List<BeanRecord> getBeanRecordList(BeanRecordQuery query);

    BigDecimal queryBeanRecords(@Param("userId") int userId, @Param("payStartTime") Date payStartTime);

    BigDecimal queryUserBeanRecords(@Param("userId") int userId);
}
