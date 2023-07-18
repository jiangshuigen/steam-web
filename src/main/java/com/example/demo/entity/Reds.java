package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Reds {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("简述")
    private String briefe;
    @ApiModelProperty("充值金额")
    private BigDecimal minRecharge;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payStartTime;
    @ApiModelProperty("红包个数")
    private int num;
    @ApiModelProperty("剩余红包个数")
    private int residue;
    @ApiModelProperty("面值(总充值%)")
    private BigDecimal percentage;
    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

}
