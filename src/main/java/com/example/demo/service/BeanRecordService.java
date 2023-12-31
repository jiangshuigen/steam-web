package com.example.demo.service;

import com.example.demo.dto.BeanRecordQuery;
import com.example.demo.dto.Callback;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.BeanRecord;
import com.example.demo.entity.UserRewardLogs;
import com.github.pagehelper.PageInfo;

import java.math.BigDecimal;
import java.util.Date;

public interface BeanRecordService {

    PageInfo<BeanRecord> getBeanRecordList(BeanRecordQuery query);

    BigDecimal queryBeanRecords(int userId, Date payStartTime);

    BigDecimal queryUserBeanRecords(int userId);

    int insertBeanReacord(BeanRecord record);

    int updateBeanRecordsStatus(Callback callback);

    BeanRecord queryBeanRecordsByCode(String api_order_id);

    BigDecimal queryAllBeanRecords(int userId);

    int saveRewardLogs(UserRewardLogs rewardLogs);

    int updateUserByTradeNo(UserDto dto, String cdk);

    BeanRecord queryBeanRecordsByTradeNo(String cdk);

    int updateBeanRecords(String orderId);

    BeanRecord getOrderInfo(String orderNo);

    BigDecimal queryPromotionAllBeanRecords(int inviterId);
}
