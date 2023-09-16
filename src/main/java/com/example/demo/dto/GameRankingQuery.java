package com.example.demo.dto;

import com.example.demo.config.DataLengh;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;

@Data
@Valid
public class GameRankingQuery {
    @ApiModelProperty("类型1-积分 2-次数")
    @DataLengh(min = 1, max = 2, message = "日期类型")
    private int type;
    @ApiModelProperty("日期类型1-今天 2-昨天")
    @DataLengh(min = 1, max = 2, message = "日期类型")
    private int dateType;
    @ApiModelProperty("列表大小")
    private int size;
}
