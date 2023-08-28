package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.math.BigDecimal;

@Data
@Builder
public class VipDto {
    @ApiModelProperty("VIP等级")
    private int level;
    @ApiModelProperty("充值门槛")
    private BigDecimal threshold;
    @ApiModelProperty("返利比率")
    private BigDecimal rebate;
    @ApiModelProperty("奖励红包")
    private BigDecimal packet;
    @ApiModelProperty("描述")
    private String description;
    @ApiModelProperty("状态 0-未领取 1-已领取 2-可领取")
    private String status;
    @Tolerate
    public VipDto() {

    }
}
