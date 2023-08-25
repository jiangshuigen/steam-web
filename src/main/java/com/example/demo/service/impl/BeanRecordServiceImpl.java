package com.example.demo.service.impl;

import com.example.demo.dto.BeanRecordQuery;
import com.example.demo.dto.Callback;
import com.example.demo.entity.BeanRecord;
import com.example.demo.entity.UserRewardLogs;
import com.example.demo.mapper.BeanRecordMapper;
import com.example.demo.service.BeanRecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class BeanRecordServiceImpl implements BeanRecordService {

    @Resource
    private BeanRecordMapper beanrecordmapper;

    @Override
    public PageInfo<BeanRecord> getBeanRecordList(BeanRecordQuery query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<BeanRecord> list = beanrecordmapper.getBeanRecordList(query);
        PageInfo<BeanRecord> listInfo = new PageInfo<>(list);
        return listInfo;
    }

    @Override
    public BigDecimal queryBeanRecords(int userId, Date payStartTime) {
        return beanrecordmapper.queryBeanRecords(userId, payStartTime);
    }

    @Override
    public BigDecimal queryUserBeanRecords(int userId) {
        return beanrecordmapper.queryUserBeanRecords(userId);
    }

    @Override
    public int insertBeanReacord(BeanRecord record) {
        return beanrecordmapper.insertBeanReacord(record);
    }

    @Override
    public int updateBeanRecordsStatus(Callback callback) {
        return beanrecordmapper.updateBeanRecordsStatus(callback);
    }

    @Override
    public BeanRecord queryBeanRecordsByCode(String api_order_id) {
        return beanrecordmapper.queryBeanRecordsByCode(api_order_id);
    }

    @Override
    public BigDecimal queryAllBeanRecords(int userId) {
        return beanrecordmapper.queryAllBeanRecords(userId);
    }

    @Override
    public int saveRewardLogs(UserRewardLogs rewardLogs) {
        return beanrecordmapper.saveRewardLogs(rewardLogs);
    }
}
