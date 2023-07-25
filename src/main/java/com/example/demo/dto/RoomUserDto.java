package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RoomUserDto {
    @ApiModelProperty("用户id")
    private int userId;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("用户头像")
    private String avatar;
}
