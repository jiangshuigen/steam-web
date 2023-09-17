package com.example.demo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleCommodityResponse {
    @ApiModelProperty("最低价")
    private BigDecimal minSellPrice;
    @ApiModelProperty("参考价")
    private BigDecimal referencePrice;
    @ApiModelProperty("在售数量")
    private BigDecimal sellNum;
}
