package com.example.demo.service.impl;

import com.example.demo.dto.BasePage;
import com.example.demo.entity.BeanChangeRecords;
import com.example.demo.mapper.BeanChangeRecordMapper;
import com.example.demo.service.BeanChangeRecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BeanChangeRecordServiceImpl implements BeanChangeRecordService {
    @Resource
    private BeanChangeRecordMapper beanchangerecordmapper;

    @Override
    public PageInfo<BeanChangeRecords> getBeanChangeRecordList(BasePage query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<BeanChangeRecords> list = beanchangerecordmapper.getBeanRecordList(query);
        PageInfo<BeanChangeRecords> listInfo = new PageInfo<>(list);
        return listInfo;
    }
}
