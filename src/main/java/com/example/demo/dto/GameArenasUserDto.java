package com.example.demo.dto;

import com.example.demo.entity.BoxRecords;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

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
    @ApiModelProperty("抽到的物品")
    private List<BoxRecords> recordList;

}
