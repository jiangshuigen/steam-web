package com.example.demo.service.impl;

import com.example.demo.config.Constant;
import com.example.demo.dto.*;
import com.example.demo.entity.BoxRecords;
import com.example.demo.entity.BoxRecordsWeb;
import com.example.demo.mapper.BoxRecordMapper;
import com.example.demo.service.BoxRecordService;
import com.example.demo.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

@Service
public class BoxRecordServiceImpl implements BoxRecordService {

    @Resource
    private BoxRecordMapper boxrecordmapper;
    @Resource
    private UserService userservice;

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

    @Override
    public List<BoxRecords> getRecordHistory(int boxId) {
        return boxrecordmapper.getRecordHistory(boxId);
    }

    @Override
    public List<BoxRecords> getRecordList(int numb) {
        if (numb <= 0) {
            numb = 50;
        }
        return boxrecordmapper.getRecordList(numb);
    }

    @Override
    @Transactional
    public int exchange(HttpServletRequest request, int[] ids) {
        int i = boxrecordmapper.exchange(ids);
        if (i > 0) {
            //查询记录列表
            BigDecimal bean = boxrecordmapper.getRecords(ids);
            //获取session
            HttpSession session = request.getSession();
            UserDto dto = (UserDto) session.getAttribute(Constant.USER_INFO);
            if (!ObjectUtils.isEmpty(dto)) {
                BigDecimal balance = dto.getBean().add(bean);
                //返回金币
                return userservice.updateBean(balance, dto.getId());
            }
        }
        return 0;
    }
}
