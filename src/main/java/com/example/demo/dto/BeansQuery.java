package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BeansQuery extends BasePage {
    @ApiModelProperty("主键id")
    private int id;
}
