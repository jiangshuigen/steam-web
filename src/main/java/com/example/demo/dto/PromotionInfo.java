package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PromotionInfo {
    @ApiModelProperty("推广码")
    private String inviterCode;
    @ApiModelProperty("推广链接(前端自行拼接一下)")
    private String inviterUrl;
    @ApiModelProperty("邀请人数")
    private int inviterNumber;
    @ApiModelProperty("邀请奖励")
    private BigDecimal inviterReward;
    @ApiModelProperty("邀请等级")
    private BigDecimal inviterLv;


}
