package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Vip {
    @ApiModelProperty("主键id")
    private int id;
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
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

}
