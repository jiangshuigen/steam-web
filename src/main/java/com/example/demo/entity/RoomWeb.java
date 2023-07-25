package com.example.demo.entity;

import com.example.demo.dto.RoomAwardWeb;
import com.example.demo.dto.RoomUserDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RoomWeb extends Room {
    @ApiModelProperty("奖品数目")
    private int awardsCount;
    @ApiModelProperty("奖金池")
    private BigDecimal awardsPool;
    @ApiModelProperty("用户头像")
    private String avatar;
    @ApiModelProperty("奖品列表")
    private List<RoomAwardWeb> awardList;
    @ApiModelProperty("玩家列表")
    private List<RoomUserDto> listUser;
}
