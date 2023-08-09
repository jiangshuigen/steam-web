package com.example.demo.mapper;

import com.example.demo.dto.*;
import com.example.demo.entity.BoxRecords;
import com.example.demo.entity.BoxRecordsWeb;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BoxRecordMapper {
    List<BoxRecords> getBoxRecordList(BoxRecordsQuery query);

    BoxRecords getRecordById(int id);

    int deleteById(int id);

    List<ApplDto> getApplyList(ApplyQuery query);

    int updateApply(ApplUpdate dto);

    List<BoxRecordsWeb> getMyPackage(BoxRecordsWebQuery query);

    int getPackage(@Param("ids") int[] ids);

    List<BoxRecordsWeb> getBackList(BackQuery query);

    int saveBoxRecord(@Param("records") List<BoxRecords> record);

    List<BoxRecords> getRecordHistory(@Param("boxId") int boxId);

    List<BoxRecords> getRecordList(int numb);

    int exchange(@Param("ids") int[] ids);

    BigDecimal getRecords(@Param("ids") int[] ids);

    int exchangeOrderNo(String merchantOrderNo);

    BoxRecords getRecordByOrderNo(String merchantOrderNo);
}
