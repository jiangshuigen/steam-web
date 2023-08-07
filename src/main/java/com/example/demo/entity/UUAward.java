package com.example.demo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UUAward {
    @ApiModelProperty("id")
    private int id;
    @ApiModelProperty("模板id")
    private int templateId;
    @ApiModelProperty("商品名称")
    private String commodityName;
    @ApiModelProperty("商品价格（单位元）")
    private String commodityPrice;
}
