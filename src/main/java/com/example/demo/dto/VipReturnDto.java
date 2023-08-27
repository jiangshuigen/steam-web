package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class VipReturnDto {
    @ApiModelProperty("累计金额")
    private BigDecimal count;
    @ApiModelProperty("下一等级")
    private int nextLevel;
    @ApiModelProperty("下一等级需要充值金额")
    private BigDecimal nextCount;
    @ApiModelProperty("VIP等级列表")
    private List<VipDto> list;
}
