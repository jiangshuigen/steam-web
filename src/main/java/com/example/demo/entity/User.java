package com.example.demo.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("用户实体类")
public class User {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("用户名")
    private String name;
    @ApiModelProperty("手机号码")
    private String mobile;
    @ApiModelProperty("脱敏后手机号码")
    private String mobileHide;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("邀请码")
    private String inviteCode;
    @ApiModelProperty("头像")
    private String avatar;
    @ApiModelProperty("邀请人员id")
    private int inviterId;
    @ApiModelProperty("邀请人员名称")
    private String inviterName;
    @ApiModelProperty("被邀请的人数")
    private int beInviteNumber;
    @ApiModelProperty("空")
    private String emailVerifiedAt;
    @ApiModelProperty("金豆")
    private BigDecimal bean;
    @ApiModelProperty("银币")
    private BigDecimal silver;
    @ApiModelProperty("积分")
    private BigDecimal integral;
    @ApiModelProperty("steam交易链接")
    private String steamUrl;
    @ApiModelProperty("steamId")
    private long steamId;
    @ApiModelProperty("推广等级")
    private int promotionLevel;
    @ApiModelProperty("VIP等级")
    private int vipLevel;
    @ApiModelProperty("首冲资格")
    private int isRecharge;

    @ApiModelProperty("赠送功能(0-开/1-关闭)")
    private Integer closeGift;
    @ApiModelProperty("提货功能(0-开/1-关闭)")
    private Integer banPickUp;

    @ApiModelProperty("是否主播")
    private Integer anchor;
    @ApiModelProperty("注册时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created_at;

    @ApiModelProperty("总充值（真实）")
    private BigDecimal truePay;
    @ApiModelProperty("下级总充值")
    private BigDecimal subordinatePay;
}
