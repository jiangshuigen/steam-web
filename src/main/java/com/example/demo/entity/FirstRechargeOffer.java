package com.example.demo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FirstRechargeOffer {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("对应金豆充值列表")
    private int beansId;
    @ApiModelProperty("对应金豆")
    private BigDecimal bean;
    @ApiModelProperty("实际RMB")
    private BigDecimal price;
    @ApiModelProperty("奖励比例")
    private int ratio;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("状态")
    private int isPutaway;
}
