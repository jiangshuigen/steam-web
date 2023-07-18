package com.example.demo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "仪表盘信息")
public class Dashboard {
    @ApiModelProperty("用户总数")
    private int userCount;
    @ApiModelProperty("总金额")
    private BigDecimal amount;//总金额
    @ApiModelProperty("提货申请")
    private int applyCount;//提货申请
    @ApiModelProperty("余额")
    private BigDecimal balance;//余额
}
