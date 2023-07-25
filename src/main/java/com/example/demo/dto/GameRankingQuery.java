package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GameRankingQuery {
    @ApiModelProperty("类型1-积分 2-次数")
    private int type;
    @ApiModelProperty("列表大小")
    private int size;
}
