package com.example.demo.dto;

import com.example.demo.entity.GameRanking;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GameRankingDto {
    @ApiModelProperty("昨日之星用户id")
    private int userId;
    @ApiModelProperty("昨日之星用户名")
    private String userName;
    @ApiModelProperty("昨日之星用户头像")
    private String avatar;
    @ApiModelProperty("昨日之星胜率")
    private BigDecimal winRate;
    @ApiModelProperty("昨日之星盈亏")
    private BigDecimal lostBean;
    @ApiModelProperty("支出")
    private BigDecimal expend;
    @ApiModelProperty("排行榜")
    private List<GameRanking> rankingList;
}
