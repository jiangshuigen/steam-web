package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.BoxRecords;
import com.example.demo.entity.BoxRecordsWeb;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface BoxRecordService {

    PageInfo<BoxRecords> getBoxRecordList(BoxRecordsQuery query);

    BoxRecords getRecordById(int id);

    int deleteById(int id);

    PageInfo<ApplDto> getApplyList(ApplyQuery query);

    int updateApply(ApplUpdate dto);

    PageInfo<BoxRecordsWeb> getMyPackage(BoxRecordsWebQuery query);

    int getPackage(int[] ids, int userId) throws Exception;

    PageInfo<BoxRecordsWeb> getBackList(HttpServletRequest request, BackQuery query);

    int saveBoxRecord(List<BoxRecords> records);

    List<BoxRecords> getRecordHistory(int boxId);

    List<BoxRecords> getRecordList(int numb);

    int exchange(HttpServletRequest request, int[] ids);
}
