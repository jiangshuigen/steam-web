package com.example.demo.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Api("查询列表")
public class BoxAwardsQuery extends BasePage {
    @ApiModelProperty("装备名称")
    private String name;
    @ApiModelProperty("宝箱id")
    private int boxId;
}
