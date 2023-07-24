package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BattleQuery extends BasePage {
    @ApiModelProperty("用户id")
    private int userId;
}
