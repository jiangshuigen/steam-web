package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserQuery extends BasePage {
    @ApiModelProperty("Id")
    private String id;
    @ApiModelProperty("用户名")
    private String name;
    @ApiModelProperty("手机号码")
    private String mobile;
    @ApiModelProperty("邀请码")
    private String inviteCode;
    @ApiModelProperty("IP")
    private String ip;
}
