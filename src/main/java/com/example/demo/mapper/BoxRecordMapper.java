package com.example.demo.mapper;

import com.example.demo.dto.*;
import com.example.demo.entity.BoxRecords;
import com.example.demo.entity.BoxRecordsWeb;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface BoxRecordMapper {
    List<BoxRecords> getBoxRecordList(BoxRecordsQuery query);

    BoxRecords getRecordById(int id);

    int deleteById(int id);

    List<ApplDto> getApplyList(ApplyQuery query);

    int updateApply(ApplUpdate dto);

    List<BoxRecordsWeb> getMyPackage(BoxRecordsWebQuery query);
}
