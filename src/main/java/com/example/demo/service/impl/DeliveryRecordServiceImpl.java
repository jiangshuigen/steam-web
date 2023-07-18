package com.example.demo.service.impl;

import com.example.demo.dto.DeliveryRecordQuery;
import com.example.demo.entity.DeliveryRecord;
import com.example.demo.entity.RedKey;
import com.example.demo.mapper.DeliveryRecordMapper;
import com.example.demo.service.DeliveryRecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DeliveryRecordServiceImpl implements DeliveryRecordService {

    @Resource
    private DeliveryRecordMapper deliveryrecordmapper;
    @Override
    public PageInfo<DeliveryRecord> getDeliveryRecordList(DeliveryRecordQuery query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<DeliveryRecord> list = deliveryrecordmapper.getDeliveryRecordList(query);
        PageInfo<DeliveryRecord> listInfo = new PageInfo<>(list);
        return listInfo;
    }
}
