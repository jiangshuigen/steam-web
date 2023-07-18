package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BasePage {
    @ApiModelProperty("pageSize")
    private int pageSize = 10;
    @ApiModelProperty("pageNo")
    private int pageNo = 1;
}
