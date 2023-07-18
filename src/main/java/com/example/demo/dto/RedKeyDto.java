package com.example.demo.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Api("口令红包")
public class RedKeyDto {
    @ApiModelProperty("面值金额")
    private BigDecimal bean;
    @ApiModelProperty("数量")
    private int numb;
}
