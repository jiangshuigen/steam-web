package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PromotionUser {
    @ApiModelProperty("邀请用户")
    private String userName;
    @ApiModelProperty("邀请奖励")
    private BigDecimal bean;
    @ApiModelProperty("时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
}
