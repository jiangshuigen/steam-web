package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BackQuery extends BasePage {
    @ApiModelProperty("用户id")
    private int userId;
}
