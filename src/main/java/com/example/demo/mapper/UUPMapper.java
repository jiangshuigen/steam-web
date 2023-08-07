package com.example.demo.mapper;

import com.example.demo.dto.UUAwardDto;
import com.example.demo.dto.UUbaseData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UUPMapper {
    int insertBaseData(@Param("list") List<UUbaseData> myObjectList);

    int insertBaseAwardData(@Param("list") List<UUAwardDto> listReturn);
}
