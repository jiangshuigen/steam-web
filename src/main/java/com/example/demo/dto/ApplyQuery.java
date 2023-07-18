package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ApplyQuery extends BasePage {
    @ApiModelProperty("申请用户")
    private String userName;
}
