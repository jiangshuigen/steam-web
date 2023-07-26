package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RoomAward {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("宝箱记录ID")
    private int boxRecordId;
    @ApiModelProperty("装备名称")
    private String name;
    @ApiModelProperty("外观")
    private String dura;
    @ApiModelProperty("封面")
    private String cover;
    @ApiModelProperty("金豆")
    private BigDecimal bean;
    @ApiModelProperty("获奖用户")
    private String userName;
    @ApiModelProperty("获奖用户头像")
    private String avatar;
    @ApiModelProperty("指定用户")
    private int designatedUser;
    @ApiModelProperty("开奖时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
}
