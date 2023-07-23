package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AwardCountDto {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("1-幸运主播 2-幸运普通 3-普通 4-主播")
    private int type;
    @ApiModelProperty("数目")
    private int numb;
}
