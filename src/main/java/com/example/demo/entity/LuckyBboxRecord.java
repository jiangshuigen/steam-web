package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class LuckyBboxRecord {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("用户id")
    private int userId;
    @ApiModelProperty("用户名称")
    private String userName;
    @ApiModelProperty("消耗金豆")
    private BigDecimal boxBean;
    @ApiModelProperty("百分比")
    private BigDecimal percent;
    @ApiModelProperty("幸运开箱ID")
    private int awardId;
    @ApiModelProperty("目标饰品")
    private String targetName;
    @ApiModelProperty("目标饰品封面")
    private String targetCover;
    @ApiModelProperty("磨损程度")
    private int awardDura;
    @ApiModelProperty("饰品等级")
    private int awardLv;
    @ApiModelProperty("目标价值")
    private BigDecimal targetValue;
    @ApiModelProperty("获得饰品ID")
    private int getAwardId;
    @ApiModelProperty("获得饰品名称")
    private String obtainName;
    @ApiModelProperty("获得饰品封面")
    private String obtainCover;
    @ApiModelProperty("获得饰品磨损程度")
    private int getAwardDura;
    @ApiModelProperty("获得饰品等级")
    private int getAwardLv;
    @ApiModelProperty("获得价值")
    private BigDecimal obtainValue;
    @ApiModelProperty("开启时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

}
