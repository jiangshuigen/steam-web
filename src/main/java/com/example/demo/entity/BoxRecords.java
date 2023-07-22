package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Api("开箱记录")
@Builder
public class BoxRecords {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("获得者")
    private int getUserId;
    @ApiModelProperty("持有者")
    private int userId;
    @ApiModelProperty("宝箱id")
    private int boxId;
    @ApiModelProperty("宝箱名称")
    private String boxName;
    @ApiModelProperty("宝箱金豆")
    private BigDecimal boxBean;
    @ApiModelProperty("奖品ID")
    private int boxAwardId;
    @ApiModelProperty("奖品名称")
    private String name;
    @ApiModelProperty("饰品唯一英文名称")
    private String hashName;
    @ApiModelProperty("奖品封面")
    private String cover;
    @ApiModelProperty("外观")
    private int dura;
    @ApiModelProperty("品质")
    private int lv;
    @ApiModelProperty("奖品金豆")
    private BigDecimal bean;
    @ApiModelProperty("订单号")
    private String code;
    @ApiModelProperty("最大T币")
    private BigDecimal maxT;
    @ApiModelProperty("唯一标识")
    private String uuid;
    @ApiModelProperty("类型")
    private int type;
    @ApiModelProperty("状态")
    private int status;
    @ApiModelProperty("回调信息")
    private String backMessage;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;


}
