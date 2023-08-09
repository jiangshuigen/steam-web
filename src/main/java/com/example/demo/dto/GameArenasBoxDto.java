package com.example.demo.dto;

import com.example.demo.entity.BoxAwards;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class GameArenasBoxDto {
    @ApiModelProperty("主键id")
    private int boxId;
    @ApiModelProperty("宝箱名称")
    private String boxName;
    @ApiModelProperty("完整封面")
    private String intactCover;
    @ApiModelProperty("随机数")
    private int arenaBoxId;
    @ApiModelProperty("武器列表")
    private List<BoxAwards> listAward;

}
