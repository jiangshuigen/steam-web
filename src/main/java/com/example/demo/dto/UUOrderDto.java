package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UUOrderDto {
    @ApiModelProperty("模板id")
    private int templateId;
}
