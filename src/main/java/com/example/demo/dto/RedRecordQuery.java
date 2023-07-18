package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RedRecordQuery extends BasePage {
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("用户名")
    private String userName;
}
