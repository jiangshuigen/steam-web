package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginInfo {
    @ApiModelProperty("用户名")
    private String name;
    @ApiModelProperty("手机号码")
    private String mobile;
    @ApiModelProperty("密码")
    private String password;
}
