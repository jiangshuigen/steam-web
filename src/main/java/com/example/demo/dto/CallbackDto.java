package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CallbackDto {
    @ApiModelProperty("消息幂等编号")
    private String messageNo;
    @ApiModelProperty("回调信息")
    private String callBackInfo;
    @ApiModelProperty("签名")
    private String sign;

}
