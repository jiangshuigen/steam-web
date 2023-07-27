package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Room {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("用户id")
    private int userId;
    @ApiModelProperty("创建人")
    private String userName;
    @ApiModelProperty("房间名称")
    private String name;
    @ApiModelProperty("说明")
    private String describe;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("结束时间")
    private Date endTime;
    @ApiModelProperty("房间人数")
    private int peopleNumber;
    @ApiModelProperty("密码")
    private String password;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("累计充值开始时间")
    private Date payStartTime;
    @ApiModelProperty("累计充值最小金额")
    private BigDecimal minRecharge;
    @ApiModelProperty("是否发放完毕")
    private int isGive;
    @ApiModelProperty("房主邀请进入0-否1-是")
    private int meInviter;
    @ApiModelProperty("置顶")
    private int top;
    @ApiModelProperty("状态  0-未开始 1-进行中 2-已结束")
    private int status;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
    @ApiModelProperty("房间类型 官方-1 主播-2")
    private String type;
}
