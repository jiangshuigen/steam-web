package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SaveAwardsDto {
    @ApiModelProperty("U品商品Id")
    private int uId;
    @ApiModelProperty("boxId")
    private int boxId;
    @ApiModelProperty("品质")
    private int lv;
    @ApiModelProperty("显示爆率")
    private int odds;
    @ApiModelProperty("投放数量")
    private int realOdds;
    @ApiModelProperty("投放数量(主播)")
    private int anchorOdds;
    @ApiModelProperty("幸运开箱 0:否 1:是")
    private int isLuckyBox;
}
