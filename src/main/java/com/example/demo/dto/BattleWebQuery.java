package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BattleWebQuery extends BasePage {
    @ApiModelProperty("状态 0:等待 1:进行中 2:已结束")
    private Integer status;
}
