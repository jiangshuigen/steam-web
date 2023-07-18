package com.example.demo.entity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Api("武器等级")
public class AwardLevels {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("等级名称")
    private String name;
    @ApiModelProperty("背景图片")
    private String bgImage;

}
