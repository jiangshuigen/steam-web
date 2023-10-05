package com.example.demo.entity;

import com.example.demo.dto.RoomAwardWeb;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class RoomWeb extends Room {
    @ApiModelProperty("奖品数目")
    private int awardsCount;
    @ApiModelProperty("奖金池")
    private BigDecimal awardsPool;
    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("房间类型 官方-1 主播-2")
    private String type;

    @ApiModelProperty("奖品列表")
    private List<RoomAwardWeb> awardList = new ArrayList<>();

}
