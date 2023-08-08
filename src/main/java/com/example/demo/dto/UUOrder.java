package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UUOrder {
    @ApiModelProperty("商品ID")
    private String commodityId;
    @ApiModelProperty("申请单号")
    private int recordId;
    @ApiModelProperty("购买价格 单位：元。")
    private String purchasePrice;
}
