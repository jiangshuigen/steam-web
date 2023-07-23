package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GameArenasUserDto {
    @ApiModelProperty("用户id")
    private int gameUserId;
    @ApiModelProperty("用户名")
    private String gameUserName;
    @ApiModelProperty("用户头像")
    private String gameAvatar;
    @ApiModelProperty("金币")
    private BigDecimal gameBean;
}
