package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UUOrderDto {
    @ApiModelProperty("商户订单号")
    private String merchantOrderNo;
    @ApiModelProperty("UU订单号")
    private String orderNo;
    @ApiModelProperty("付款金额")
    private String payAmount;
    @ApiModelProperty("状态")
    private String orderStatus;
}
