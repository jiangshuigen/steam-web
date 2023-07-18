package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BeanChangeRecords {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("最终金额")
    private BigDecimal finalBean;
    @ApiModelProperty("金额")
    private BigDecimal bean;
    @ApiModelProperty("类型 0-支付 1-收入")
    private int type;
    @ApiModelProperty("类型")
    private int changeType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建事假")
    private Date createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("修改时间")
    private Date updatedAt;

}
