package com.example.demo.service.impl;

import com.example.demo.dto.RedRecordQuery;
import com.example.demo.entity.RedRecords;
import com.example.demo.mapper.RedRecordsMapper;
import com.example.demo.service.RedRecordsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RedRecordsServiceImpl implements RedRecordsService {

    @Resource
    private RedRecordsMapper redrecordsmapper;

    @Override
    public PageInfo<RedRecords> getRecordsListByPage(RedRecordQuery query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<RedRecords> list = redrecordsmapper.getRecordsListByPage(query);
        PageInfo<RedRecords> listInfo = new PageInfo<>(list);
        return listInfo;
    }
}
