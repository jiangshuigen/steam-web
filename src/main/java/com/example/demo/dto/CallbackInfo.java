package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CallbackInfo {
    @ApiModelProperty("订单号")
    private String orderNo;
    @ApiModelProperty("预留字段")
    private int orderType;
    @ApiModelProperty("预留字段")
    private int orderSubType;
    @ApiModelProperty("购买用户编号")
    private int buyerUserId;
    @ApiModelProperty("订单大状态")
    private int orderStatus;
    @ApiModelProperty("订单小状态")
    private int orderSubStatus;
    @ApiModelProperty("第三方商户单号")
    private String merchantOrderNo;
    @ApiModelProperty("通知类型(1:等待发货，2:等待收货，3:购买成功，4:订单取消)")
    private int notifyType;
    @ApiModelProperty("通知类型描述(等待发货，等待收货，购买成功，订单取消)")
    private String notifyDesc;
}
