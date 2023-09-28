package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserExchangeDetail {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("用户id")
    private int userId;
    @ApiModelProperty("金额")
    private BigDecimal bean;
    @ApiModelProperty("类型 1-金币转银币 2-银币转金币")
    private Integer type;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
