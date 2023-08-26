package com.example.demo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RechangeWelfare {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("主体")
    private String title;
    @ApiModelProperty("状态 0-未领取 1-已领取 2-可领取")
    private String status;
    @ApiModelProperty("奖励范围")
    private String reward;
    @ApiModelProperty("充值门槛")
    private int cost;
    @ApiModelProperty("我的累计充值")
    private int mycost;
}
