package com.example.demo.service.impl;

import com.example.demo.dto.*;
import com.example.demo.entity.BoxRecords;
import com.example.demo.entity.BoxRecordsWeb;
import com.example.demo.entity.RedRecords;
import com.example.demo.mapper.BoxRecordMapper;
import com.example.demo.service.BoxRecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BoxRecordServiceImpl implements BoxRecordService {

    @Resource
    private BoxRecordMapper boxrecordmapper;

    @Override
    public PageInfo<BoxRecords> getBoxRecordList(BoxRecordsQuery query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<BoxRecords> list = boxrecordmapper.getBoxRecordList(query);
        PageInfo<BoxRecords> listInfo = new PageInfo<>(list);
        return listInfo;
    }

    @Override
    public BoxRecords getRecordById(int id) {
        return boxrecordmapper.getRecordById(id);
    }

    @Override
    public int deleteById(int id) {
        return boxrecordmapper.deleteById(id);
    }

    @Override
    public PageInfo<ApplDto> getApplyList(ApplyQuery query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<ApplDto> list = boxrecordmapper.getApplyList(query);
        PageInfo<ApplDto> listInfo = new PageInfo<>(list);
        return listInfo;
    }

    @Override
    public int updateApply(ApplUpdate dto) {
        return boxrecordmapper.updateApply(dto);
    }

    @Override
    public PageInfo<BoxRecordsWeb> getMyPackage(BoxRecordsWebQuery query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<BoxRecordsWeb> list = boxrecordmapper.getMyPackage(query);
        PageInfo<BoxRecordsWeb> listInfo = new PageInfo<>(list);
        return listInfo;
    }
}
