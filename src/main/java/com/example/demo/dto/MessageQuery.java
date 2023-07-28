package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MessageQuery extends BasePage{
    @ApiModelProperty("状态 1-已读 0-未读")
    private Integer status;
    @ApiModelProperty("用户id")
    private int userId;
}
