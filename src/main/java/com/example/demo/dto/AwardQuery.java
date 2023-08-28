package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AwardQuery extends BasePage {
    @ApiModelProperty("装备名称")
    private String name;
}
