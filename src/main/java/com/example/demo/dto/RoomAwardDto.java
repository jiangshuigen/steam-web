package com.example.demo.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RoomAwardDto {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("房间ID")
    private int roomId;
    @ApiModelProperty("记录id")
    private int boxRecordId;
    @ApiModelProperty("获得用户ID")
    private int getUserId;
}
