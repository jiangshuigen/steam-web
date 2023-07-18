package com.example.demo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PromotionLevels {
    private int id;
    @ApiModelProperty("称谓")
    private String name;
    @ApiModelProperty("推广等级")
    private int level;
    @ApiModelProperty("有效邀请")
    private int inviteNum;
    @ApiModelProperty("下级累积充值")
    private BigDecimal inviteTotal;
    @ApiModelProperty("累计充值")
    private BigDecimal total;
    @ApiModelProperty("推广回扣")
    private BigDecimal rebate;
    @ApiModelProperty("注册赠送")
    private BigDecimal regRebate;
    @ApiModelProperty("描述")
    private String description;
}
