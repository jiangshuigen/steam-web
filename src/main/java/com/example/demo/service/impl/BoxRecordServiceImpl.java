package com.example.demo.service.impl;

import com.example.demo.config.Constant;
import com.example.demo.dto.*;
import com.example.demo.entity.BoxRecords;
import com.example.demo.entity.BoxRecordsWeb;
import com.example.demo.mapper.BoxRecordMapper;
import com.example.demo.service.BoxRecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

    @Override
    public int getPackage(int[] ids) {
        return boxrecordmapper.getPackage(ids);
    }

    @Override
    public PageInfo<BoxRecordsWeb> getBackList(HttpServletRequest request, BackQuery query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        //获取session
        HttpSession session = request.getSession();
        UserDto dto = (UserDto) session.getAttribute(Constant.USER_INFO);
        if (!ObjectUtils.isEmpty(dto)) {
            query.setUserId(dto.getId());
            List<BoxRecordsWeb> list = boxrecordmapper.getBackList(query);
            PageInfo<BoxRecordsWeb> listInfo = new PageInfo<>(list);
            return listInfo;
        } else {
            return null;
        }

    }

    @Override
    public int saveBoxRecord(List<BoxRecords> records) {
        return boxrecordmapper.saveBoxRecord(records);
    }
}
