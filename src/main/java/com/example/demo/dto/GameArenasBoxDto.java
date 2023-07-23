package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GameArenasBoxDto {
    @ApiModelProperty("主键id")
    private int boxId;
    @ApiModelProperty("宝箱名称")
    private String boxName;
    @ApiModelProperty("完整封面")
    private String intactCover;
}
