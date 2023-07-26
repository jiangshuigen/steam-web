package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OpenDto {
    @ApiModelProperty("奖品ID")
    private int awardId;
    @ApiModelProperty("幸运值")
    private BigDecimal percent;
}
