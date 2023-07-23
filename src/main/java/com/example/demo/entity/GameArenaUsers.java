package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class GameArenaUsers {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("对战ID")
    private int gameArenaId;
    @ApiModelProperty("用户ID")
    private int userId;
    @ApiModelProperty("座位")
    private int seat;
    @ApiModelProperty("价值")
    private BigDecimal worth;
    @ApiModelProperty("赢得价值")
    private BigDecimal winWorth;
    @ApiModelProperty("赢了 0：否 1：是")
    private int isWin;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

}
