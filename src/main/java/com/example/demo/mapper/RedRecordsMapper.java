package com.example.demo.mapper;

import com.example.demo.dto.RedRecordQuery;
import com.example.demo.entity.RedRecords;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RedRecordsMapper {
    List<RedRecords> getRecordsListByPage(RedRecordQuery query);

    int saveRecords(RedRecords redRecords);

    List<RedRecords> getRecord(@Param("redId") int redId, @Param("userId") int userId, @Param("type") int type);
}
