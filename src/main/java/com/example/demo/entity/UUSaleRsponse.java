package com.example.demo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UUSaleRsponse {
    private SaleTemplateResponse saletemplateresponse;
    private SaleCommodityResponse salecommodityresponse;
}

@Data
class SaleTemplateResponse {
    @ApiModelProperty("模板id")
    private int templateId;
    @ApiModelProperty("模板hashName")
    private String templateHashName;
    @ApiModelProperty("模板图片链接")
    private String iconUrl;
    @ApiModelProperty("外观名称")
    private String exteriorName;
    @ApiModelProperty("品质")
    private String rarityName;
    @ApiModelProperty("类别")
    private String qualityName;
}

@Data
class SaleCommodityResponse {
    @ApiModelProperty("最低价")
    private BigDecimal minSellPrice;
    @ApiModelProperty("参考价")
    private BigDecimal referencePrice;
    @ApiModelProperty("在售数量")
    private BigDecimal sellNum;
}
