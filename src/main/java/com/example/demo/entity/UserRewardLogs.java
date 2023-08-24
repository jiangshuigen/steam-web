package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class UserRewardLogs {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("用户id")
    private int userId;
    @ApiModelProperty("类型 1:注册奖励 2:下级充值奖励 3:首充奖励 4:累计充值返佣 5:解冻注册赠送金豆 ")
    private int type;
    @ApiModelProperty("下级用户ID")
    private int nextUserId;
    @ApiModelProperty("获得金豆")
    private BigDecimal bean;
    @ApiModelProperty("下级金豆")
    private BigDecimal chargeBean;
    @ApiModelProperty("创建")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    @Tolerate
    public UserRewardLogs() {
    }
}
