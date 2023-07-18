package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeliveryRecordQuery extends BasePage {
    @ApiModelProperty("用户id")
    private int userId;
    @ApiModelProperty("用户名称")
    private String userName;
    @ApiModelProperty("饰品名称")
    private String name;
}
