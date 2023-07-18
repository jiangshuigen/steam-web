package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class Cards {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("卡号")
    private String number;
    @ApiModelProperty("金豆")
    private BigDecimal bean;
    @ApiModelProperty("状态0-未使用 ，1-已使用")
    private int status;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

}
