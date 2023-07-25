package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class GameRanking {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("用户id")
    private int userId;

    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("总场次")
    private int total;
    @ApiModelProperty("胜利场次")
    private int win;
    @ApiModelProperty("失败场次")
    private int fail;
    @ApiModelProperty("胜率")
    private BigDecimal winRate;
    @ApiModelProperty("支出")
    private BigDecimal expend;
    @ApiModelProperty("收入")
    private BigDecimal income;
    @ApiModelProperty("盈亏")
    private BigDecimal lostBean;
    @ApiModelProperty("时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;

}
