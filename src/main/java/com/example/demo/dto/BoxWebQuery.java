package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BoxWebQuery {
    @ApiModelProperty("分类")
    private String cateId;
}
