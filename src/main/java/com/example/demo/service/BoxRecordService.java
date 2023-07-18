package com.example.demo.service;

import com.example.demo.dto.ApplDto;
import com.example.demo.dto.ApplUpdate;
import com.example.demo.dto.ApplyQuery;
import com.example.demo.dto.BoxRecordsQuery;
import com.example.demo.entity.BoxRecords;
import com.github.pagehelper.PageInfo;

public interface BoxRecordService {

    PageInfo<BoxRecords> getBoxRecordList(BoxRecordsQuery query);

    BoxRecords getRecordById(int id);

    int deleteById(int id);

    PageInfo<ApplDto>  getApplyList(ApplyQuery query);

    int updateApply(ApplUpdate dto);
}
