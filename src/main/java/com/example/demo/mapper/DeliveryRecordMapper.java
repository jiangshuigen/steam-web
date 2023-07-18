package com.example.demo.mapper;

import com.example.demo.dto.DeliveryRecordQuery;
import com.example.demo.entity.DeliveryRecord;

import java.util.List;

public interface DeliveryRecordMapper {
    List<DeliveryRecord> getDeliveryRecordList(DeliveryRecordQuery query);
}
