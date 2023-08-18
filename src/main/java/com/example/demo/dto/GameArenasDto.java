package com.example.demo.dto;

import com.example.demo.entity.BoxRecords;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class GameArenasDto {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("创建用户")
    private int createUserId;
    @ApiModelProperty("人数")
    private int userNum;
    @ApiModelProperty("箱子数")
    private int boxNum;
    @ApiModelProperty("总价值")
    private BigDecimal totalBean;
    @ApiModelProperty("状态 0:等待 1:进行中 2:已结束")
    private int status;
    @ApiModelProperty("获胜用户")
    private String winUserId;
    @ApiModelProperty("开奖编号")
    private String drawCode;
    @ApiModelProperty("创建")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("用户头像")
    private String avatar;
    @ApiModelProperty("箱子图片")
    private List<GameArenasBoxDto> listBox;
    @ApiModelProperty("玩家列表")
    private List<GameArenasUserDto> listUser;
    @ApiModelProperty("抽到的物品列表（总）")
    private List<BoxRecords> recordList;
    @ApiModelProperty("胜利获得")
    private BigDecimal winnerBean;

}
