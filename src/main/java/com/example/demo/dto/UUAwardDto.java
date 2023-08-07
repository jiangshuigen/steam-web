package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UUAwardDto {

    @ApiModelProperty("模板id")
    private int templateId;
    @ApiModelProperty("模板hash name")
    private String templateHashName;
    @ApiModelProperty("模版名称")
    private String templateName;
    @ApiModelProperty("模板图片")
    private String iconUrl;
    @ApiModelProperty("外观名称")
    private String exteriorName;
    @ApiModelProperty("品质")
    private String rarityName;
    @ApiModelProperty("武器大类")
    private int typeId;
    @ApiModelProperty("武器类型ID")
    private int weaponId;
    @ApiModelProperty("武器大类hashName")
    private String typeHashName;
    @ApiModelProperty("在售最低价(单位元)")
    private String minSellPrice;
    @ApiModelProperty("模板参考价(单位：元)")
    private String referencePrice;
    @ApiModelProperty("在售数量")
    private int sellNum;
    @ApiModelProperty("武器类型标签hashName")
    private String weaponHashName;
}
