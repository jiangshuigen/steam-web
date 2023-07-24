package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class GameAwardRecords {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("对战ID")
    private int gameArenaId;
    @ApiModelProperty("用户id")
    private int userId;
    @ApiModelProperty("奖励ID")
    private int awardId;
    @ApiModelProperty("状态 0:待开奖 1:已结束")
    private int status;
    @ApiModelProperty("开启时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
}
