package com.example.demo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Box {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("武器名称")
    private String name;
    @ApiModelProperty("武器图片")
    private String cover;
    @ApiModelProperty("武器封面")
    private String weaponCover;
    @ApiModelProperty("完整封面")
    private String intactCover;
    @ApiModelProperty("消耗金豆")
    private BigDecimal bean;
    @ApiModelProperty("对战消耗金豆")
    private BigDecimal gameBean;
    @ApiModelProperty("宝箱分类id")
    private int cateId;
    @ApiModelProperty("排序")
    private int sort;
    @ApiModelProperty("是否上架")
    private int isPutaway;
    @ApiModelProperty("幸运区间")
    private String luckInterval;
    @ApiModelProperty("幸运区间（主播）")
    private String luckIntervalAnchor;
}
