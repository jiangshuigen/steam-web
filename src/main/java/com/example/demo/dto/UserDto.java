package com.example.demo.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Api("前台展示")
public class UserDto {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("用户名")
    private String name;
    @ApiModelProperty("手机号码")
    private String mobile;
    @ApiModelProperty("脱敏后手机号码")
    private String mobileHide;
    @ApiModelProperty("邀请码")
    private String inviteCode;
    @ApiModelProperty("邀请人员id")
    private int inviterId;
    @ApiModelProperty("头像")
    private String avatar;
    @ApiModelProperty("VIP等级")
    private int vipLevel;
    @ApiModelProperty("是否主播 0：否 1：是")
    private int anchor;
    @ApiModelProperty("金豆")
    private BigDecimal bean;
    @ApiModelProperty("银币")
    private BigDecimal silver;
    @ApiModelProperty("总充值（统计）")
    private BigDecimal totalRecharge;
    @ApiModelProperty("下一级VIP经验")
    private BigDecimal nextVipLevelEXP;
    @ApiModelProperty("steam交易链接")
    private String steamUrl;
    @ApiModelProperty("pwd")
    private String pwd;
}
