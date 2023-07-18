package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CardsQuery extends BasePage {
    @ApiModelProperty("卡号")
    private String number;
}
