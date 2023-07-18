package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RoomAwardQuery extends BasePage {
    @ApiModelProperty("房间号")
    private int roomId;
}
