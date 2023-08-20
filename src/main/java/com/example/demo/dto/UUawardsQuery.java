package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UUawardsQuery extends BasePage {
    @ApiModelProperty("装备名称")
    private String name;
    @ApiModelProperty("uu武器大类")
    private int uuTypeId;
    @ApiModelProperty("uu武器小类")
    private int uuWeaponId;
    @ApiModelProperty("排序 0-正序 1-倒序")
    private int sort;
    @ApiModelProperty("价格区间最低")
    private int min;
    @ApiModelProperty("价格区间最高")
    private int max;
    @ApiModelProperty("商城 0-正序 1-倒序")
    private int isShop;
    @ApiModelProperty("对战 0-正序 1-倒序")
    private int isBattle;
}
