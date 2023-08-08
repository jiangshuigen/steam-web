package com.example.demo.mapper;

import com.example.demo.dto.UUAwardDto;
import com.example.demo.dto.UUbaseData;
import com.example.demo.entity.DeliveryRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UUPMapper {
    int insertBaseData(@Param("list") List<UUbaseData> myObjectList);

    int insertBaseAwardData(@Param("list") List<UUAwardDto> listReturn);

    int updateStatusByOrderNo(String merchantOrderNo);

    int addDeliveryRecords(DeliveryRecord deliveryrecord);
}
