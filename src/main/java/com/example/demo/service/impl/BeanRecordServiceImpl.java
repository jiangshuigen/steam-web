package com.example.demo.service.impl;

import com.example.demo.dto.BeanRecordQuery;
import com.example.demo.entity.BeanRecord;
import com.example.demo.mapper.BeanRecordMapper;
import com.example.demo.service.BeanRecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
}
