package com.example.demo.entity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Api("背包记录")
public class BoxRecordsWeb {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("获得者")
    private int getUserId;
    @ApiModelProperty("持有者")
    private int userId;
    @ApiModelProperty("奖品名称")
    private String name;
    @ApiModelProperty("品质")
    private int lv;
    @ApiModelProperty("奖品封面")
    private String cover;
    @ApiModelProperty("外观")
    private int dura;
    @ApiModelProperty("奖品金豆")
    private BigDecimal bean;
}
