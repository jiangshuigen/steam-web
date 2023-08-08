package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ApplDto {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("获得者")
    private int getUserId;
    @ApiModelProperty("申请用户")
    private String userName;
    @ApiModelProperty("steam交易链接")
    private String steamUrl;
    @ApiModelProperty("主播 0：否 1：是")
    private int anchor;
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
    @ApiModelProperty("状态 0：待操作 1：领取成功 2：兑换成功 4：申请提货")
    private int status;
    @ApiModelProperty("回调信息")
    private String backMessage;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
}
