package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BeanRecord {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("用户id")
    private int userId;
    @ApiModelProperty("用户名")
    private String userName;
    private int inviterId;
    @ApiModelProperty("c币")
    private BigDecimal bean;
    private BigDecimal price;
    private BigDecimal finallyPrice;
    @ApiModelProperty("订单号")
    private String code;
    @ApiModelProperty("支付平台单号")
    private String tradeNo;
    @ApiModelProperty("状态")
    private int status;
    private int isBenefit;
    private int isPayApi;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("下单时间")
    private Date createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("修改时间")
    private Date updatedAt;

}
