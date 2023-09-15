package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SellerRecordQuery {
    @ApiModelProperty("商品模板id")
    private int templateId;
    @ApiModelProperty("商品模板hashName")
    private String templateHashName;
}
