package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RoomQuery extends BasePage{
    @ApiModelProperty("ID")
    private int id;
}
