package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExchangeDto {
    @ApiModelProperty("金额")
    private BigDecimal count;
    @ApiModelProperty("类型 1-金币转银币 2-银币转金币")
    private Integer type;
}
