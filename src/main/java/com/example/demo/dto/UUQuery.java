package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UUQuery extends BasePage {
    @ApiModelProperty("uu武器大类")
    private int uuTypeId;
}
