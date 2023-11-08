package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserRegisterDto {
    @ApiModelProperty("用户名")
    private String name;
    @ApiModelProperty("手机号码")
    private String mobile;
    @ApiModelProperty("邀请码")
    private String inviteCode;
    @ApiModelProperty("邀请码")
    private int inviterId;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("验证码")
    private String code;
    @ApiModelProperty("短信验证码")
    private String mobileCode;

    @ApiModelProperty("随机奖励")
    private BigDecimal bean;
}
