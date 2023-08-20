package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UUawardsDto {
    @ApiModelProperty("ID")
    private int id;
    @ApiModelProperty("itemId")
    private int itemId;
    @ApiModelProperty("饰品中文名")
    private String name;
    @ApiModelProperty("饰品中文名+外观")
    private String itemName;
    @ApiModelProperty("饰品唯一英文名称")
    private String marketHashName;
    @ApiModelProperty("饰品中文短名称")
    private String shortName;
    @ApiModelProperty("CDN饰品图片Url")
    private String imageUrl;
    @ApiModelProperty("本地饰品图片Url")
    private String localImageUrl;
    @ApiModelProperty("饰品外观")
    private String dura;
    @ApiModelProperty("uu武器大类")
    private int uuTypeId;
    @ApiModelProperty("uu武器小类")
    private int uuWeaponId;
    @ApiModelProperty("金币")
    private BigDecimal bean;
    @ApiModelProperty("是否商城 1-是 0-否")
    private int isShop;
    @ApiModelProperty("是否对战 1-是 0-否")
    private int isBattle;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
}
