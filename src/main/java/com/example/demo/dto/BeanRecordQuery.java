package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

@Data
public class BeanRecordQuery extends BasePage {
    @ApiModelProperty("用户id")
    private int userId;
    @ApiModelProperty("用户名称")
    private String userName;
    @ApiModelProperty("主播id")
    private int inviterId;
    @ApiModelProperty("订单号")
    private String code;
    @ApiModelProperty("开始时间")
    private String beginTime;
    @ApiModelProperty("结束时间")
    private String endTime;
    @ApiModelProperty("状态 0-未付款 1-已付款")
    private Integer status;
}
