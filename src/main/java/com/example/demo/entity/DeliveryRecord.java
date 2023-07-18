package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Api("发货管理实体类")
public class DeliveryRecord {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("用户id")
    private int userId;
    @ApiModelProperty("用户名称")
    private String userName;
    @ApiModelProperty("仓库id")
    private int recordId;
    @ApiModelProperty("物品名称")
    private String recordName;
    @ApiModelProperty("奖品封面")
    private String cover;
    @ApiModelProperty("仓库单号")
    private String recordCode;
    @ApiModelProperty("ZBT成交单号")
    private String tradeNo;
    @ApiModelProperty("实际支付金额")
    private BigDecimal price;
    @ApiModelProperty("发货模式 1-人工 2-自动")
    private int delivery;
    @ApiModelProperty("ZBT返回订单ID")
    private String orderId;
    @ApiModelProperty("状态")
    private int zbtStatus;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

}
