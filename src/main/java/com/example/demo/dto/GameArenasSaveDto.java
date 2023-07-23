package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GameArenasSaveDto {
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
    @ApiModelProperty("箱子")
    private List<Integer> listBox;

}
