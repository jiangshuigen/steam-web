package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LuckyBoxRecordQuery extends BasePage {
    @ApiModelProperty("用户")
    private String userName;
    @ApiModelProperty("目标饰品")
    private String awardName;
}
