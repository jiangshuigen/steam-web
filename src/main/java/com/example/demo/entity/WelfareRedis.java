package com.example.demo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class WelfareRedis {
    @ApiModelProperty("用户id")
    private int userId;
    @ApiModelProperty("充值金币")
    private int cost;
    @ApiModelProperty("领取记录")
    private List<Integer> list;
}
