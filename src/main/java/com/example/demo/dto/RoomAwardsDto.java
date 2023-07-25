package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RoomAwardsDto {
    @ApiModelProperty("房间ID")
    private int roomId;
    @ApiModelProperty("记录id")
    private int boxRecordId;

}
