package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UUbaseData {
    @ApiModelProperty("ID")
    private int id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("英文名")
    private String hashName;
}
