package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OpenBox {
    @ApiModelProperty("宝箱ID")
    private int boxId;
    @ApiModelProperty("购买的数量")
    private int numb;
}
