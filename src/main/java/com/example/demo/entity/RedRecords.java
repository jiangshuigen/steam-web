package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class RedRecords {
    @ApiModelProperty("主键")
    private int id;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("金额")
    private BigDecimal bean;
    @ApiModelProperty("类型：1:红包活动 2:口令红包")
    private int type;
    @ApiModelProperty("时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @ApiModelProperty("红包id")
    private int redId;
    @ApiModelProperty("用户id")
    private int userId;
    @Tolerate
    public RedRecords() {
    }
}
