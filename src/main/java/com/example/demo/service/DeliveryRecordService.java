package com.example.demo.service;

import com.example.demo.dto.DeliveryRecordQuery;
import com.example.demo.entity.DeliveryRecord;
import com.github.pagehelper.PageInfo;

public interface DeliveryRecordService {

    PageInfo<DeliveryRecord> getDeliveryRecordList(DeliveryRecordQuery query);
}
