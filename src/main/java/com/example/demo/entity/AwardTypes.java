package com.example.demo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AwardTypes {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("封面")
    private String cover;
    @ApiModelProperty("排序")
    private int sort;

}
