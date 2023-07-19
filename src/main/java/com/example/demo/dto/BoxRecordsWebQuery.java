package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BoxRecordsWebQuery extends BasePage {
    @ApiModelProperty("用户id")
    private int userId;
    @ApiModelProperty("价值排序 0-从小到大 1-从大到小")
    private int orderV;
}
