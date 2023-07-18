package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ApplUpdate {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("状态 0：待操作 1：领取成功 2：兑换成功 4：申请提货")
    private int status;
}
