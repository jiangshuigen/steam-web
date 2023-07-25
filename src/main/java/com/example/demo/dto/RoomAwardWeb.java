package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomAwardWeb {
    @ApiModelProperty("主键id")
    private int roomAwardId;
    @ApiModelProperty("装备名称")
    private String roomAwardName;
    @ApiModelProperty("封面")
    private String roomAwardDura;
    @ApiModelProperty("金豆")
    private BigDecimal roomAwardBean;
}
