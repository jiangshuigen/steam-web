package com.example.demo.service.pay;

import com.example.demo.dto.AliPayOrderInfo;
import com.example.demo.dto.Callback;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;

import javax.servlet.http.HttpServletRequest;

public interface PayService {
    AliPayOrderInfo getOrderNumber(User usr, int count,HttpServletRequest request);

    int updateBeanRecord(Callback callback);

    int updateUserByTradeNo(UserDto dto, String cdk)throws Exception;
}
