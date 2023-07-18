package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BoxRecordsQuery extends BasePage{
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("用户id")
    private int userId;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("类型：1-开箱记录 ，3-对战开箱， 4-幸运开箱")
    private int type;

}
