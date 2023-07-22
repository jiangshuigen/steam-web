package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

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
    @ApiModelProperty("订单号")
    private String code;
    @ApiModelProperty("状态 status 0：待操作 1：领取成功 2：兑换成功 4：申请提货")
    private int status;
    @ApiModelProperty("取回发起时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateAt;
}
