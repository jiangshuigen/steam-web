package com.example.demo.service.impl;

import com.example.demo.dto.DeliveryRecordQuery;
import com.example.demo.dto.UUResponse;
import com.example.demo.entity.DeliveryRecord;
import com.example.demo.mapper.DeliveryRecordMapper;
import com.example.demo.service.DeliveryRecordService;
import com.example.demo.util.JacksonUtils;
import com.example.demo.util.OkHttpUtil;
import com.example.demo.util.RSAUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public List<DeliveryRecord> getSellList(DeliveryRecordQuery query) {
        String result = OkHttpUtil.builder().url("http://gw-openapi.youpin898.com/open/v1/api/goodsQuery")
                .addParam("id", "1")
                .initPost(false)
                .sync();
        return null;
    }


}
