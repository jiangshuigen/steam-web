package com.example.demo.mapper;

import com.example.demo.dto.ApplDto;
import com.example.demo.dto.ApplUpdate;
import com.example.demo.dto.ApplyQuery;
import com.example.demo.dto.BoxRecordsQuery;
import com.example.demo.entity.BoxRecords;

import java.util.List;

public interface BoxRecordMapper {
    List<BoxRecords> getBoxRecordList(BoxRecordsQuery query);

    BoxRecords getRecordById(int id);

    int deleteById(int id);

    List<ApplDto> getApplyList(ApplyQuery query);

    int updateApply(ApplUpdate dto);
}
