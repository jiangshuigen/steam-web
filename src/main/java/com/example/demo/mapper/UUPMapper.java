package com.example.demo.mapper;

import com.example.demo.dto.UUAwardDto;
import com.example.demo.dto.UUawardsDto;
import com.example.demo.dto.UUbaseData;
import com.example.demo.entity.DeliveryRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UUPMapper {
    int insertBaseData(@Param("list") List<UUbaseData> myObjectList);

    int insertBaseAwardData(@Param("list") List<UUAwardDto> listReturn);

    int updateStatusByOrderNo(@Param("merchantOrderNo")String merchantOrderNo,@Param("status")String status);

    int addDeliveryRecords(DeliveryRecord deliveryrecord);

    int updateStatus(@Param("orderNo") String orderNo,@Param("status") String status);

    List<UUawardsDto> queryAllUUAwardList();

    void updateAwardsBean(UUawardsDto uUawardsDto);
}
