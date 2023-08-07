package com.example.demo.service;

import com.example.demo.dto.DeliveryRecordQuery;
import com.example.demo.dto.UUResponse;
import com.example.demo.entity.DeliveryRecord;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface DeliveryRecordService {

    PageInfo<DeliveryRecord> getDeliveryRecordList(DeliveryRecordQuery query);

    List<DeliveryRecord> getSellList(DeliveryRecordQuery query);


}
