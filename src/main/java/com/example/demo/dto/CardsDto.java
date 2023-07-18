package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CardsDto {
    @ApiModelProperty("面值")
    private BigDecimal bean;
    @ApiModelProperty("卡密列表")
    private List<String> codeList;
}
