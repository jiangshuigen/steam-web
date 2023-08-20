package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RedQuery extends BasePage {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("是否结束0-否 1-是")
    private int status;
}
