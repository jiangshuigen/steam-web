package com.example.demo.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Api("用户信息修改字段")
public class UserUpdateDto {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("用户名")
    private String name;
    @ApiModelProperty("头像")
    private String avatar;
    @ApiModelProperty("steam交易链接")
    private String steamUrl;
}
