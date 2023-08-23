package com.example.demo.service.pay;

import com.example.demo.dto.AliPayOrderInfo;
import com.example.demo.dto.Callback;
import com.example.demo.entity.User;

public interface PayService {
    AliPayOrderInfo getOrderNumber(User usr, int count);

    int updateBeanRecord(Callback callback);
}
