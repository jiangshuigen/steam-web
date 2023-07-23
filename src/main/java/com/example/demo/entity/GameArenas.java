package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class GameArenas {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("创建用户")
    private int createUserId;
    @ApiModelProperty("人数")
    private int userNum;
    @ApiModelProperty("箱子数")
    private int box_num;
    @ApiModelProperty("总价值")
    private BigDecimal totalBean;
    @ApiModelProperty("状态 0:等待 1:进行中 2:已结束")
    private int status;
    @ApiModelProperty("获胜用户")
    private int winUserId;
    @ApiModelProperty("开奖编号")
    private String draw_code;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
    @ApiModelProperty("箱子")
    private List<Integer> listBox;
}
