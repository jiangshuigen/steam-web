package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.BoxRecords;
import com.example.demo.entity.BoxRecordsWeb;
import com.github.pagehelper.PageInfo;

public interface BoxRecordService {

    PageInfo<BoxRecords> getBoxRecordList(BoxRecordsQuery query);

    BoxRecords getRecordById(int id);

    int deleteById(int id);

    PageInfo<ApplDto>  getApplyList(ApplyQuery query);

    int updateApply(ApplUpdate dto);

    PageInfo<BoxRecordsWeb>  getMyPackage(BoxRecordsWebQuery query);
}
