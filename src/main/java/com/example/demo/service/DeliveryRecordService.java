package com.example.demo.service;

import com.example.demo.dto.DeliveryRecordQuery;
import com.example.demo.dto.SellerRecordQuery;
import com.example.demo.dto.UUResponse;
import com.example.demo.entity.DeliveryRecord;
import com.example.demo.entity.UUSaleRsponse;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface DeliveryRecordService {

    PageInfo<DeliveryRecord> getDeliveryRecordList(DeliveryRecordQuery query);

    UUSaleRsponse getSellList(SellerRecordQuery query);


}
