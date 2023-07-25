package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class JoinRoomDto {
    @ApiModelProperty(value = "用户id", required = false)
    private int userId;
    @ApiModelProperty(value = "房间号", required = true)
    private int roomId;
    @ApiModelProperty(value = "密码", required = false)
    private String password;
}
