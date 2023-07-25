package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WebRoomQuery {
    @ApiModelProperty("用户Id")
    private int userId;
    @ApiModelProperty("状态  0-未开始 1-进行中 2-已结束")
    private int status;
}
