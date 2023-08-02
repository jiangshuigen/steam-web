package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WebBoxAwardsQuery extends BasePage{
    @ApiModelProperty("装备名称")
    private String name;
    @ApiModelProperty("装备类型")
    private int type;
    @ApiModelProperty("排序 0-正序 1-倒序")
    private int sort;
    @ApiModelProperty("价格区间最低")
    private int min;
    @ApiModelProperty("价格区间最高")
    private int max;
}
