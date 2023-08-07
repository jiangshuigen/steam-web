package com.example.demo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

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
    @ApiModelProperty("uu武器大类")
    private int uuTypeId;
    @ApiModelProperty("uu武器小类")
    private int uuWeaponId;

    @ApiModelProperty("子类型")
    private List<AwardTypes> list;

}
