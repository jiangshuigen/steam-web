package com.example.demo.mapper;

import com.example.demo.dto.BeanRecordQuery;
import com.example.demo.dto.Callback;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.BeanRecord;
import com.example.demo.entity.UserRewardLogs;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface BeanRecordMapper {
    List<BeanRecord> getBeanRecordList(BeanRecordQuery query);

    BigDecimal queryBeanRecords(@Param("userId") int userId, @Param("payStartTime") Date payStartTime);

    BigDecimal queryUserBeanRecords(@Param("userId") int userId);

    int insertBeanReacord(BeanRecord record);

    int updateBeanRecordsStatus(Callback callback);

    BeanRecord queryBeanRecordsByCode(String code);

    BigDecimal queryAllBeanRecords(int userId);

    int saveRewardLogs(UserRewardLogs rewardLogs);

    int updateUserByTradeNo(@Param("user") UserDto dto, @Param("cdk") String cdk);

    BeanRecord queryBeanRecordsByTradeNo(String cdk);

    int updateBeanRecords(String orderId);
}
