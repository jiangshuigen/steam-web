package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class GameArenaBox {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("对战ID")
    private int gameArenaId;
    @ApiModelProperty("箱子ID")
    private int boxId;
    @ApiModelProperty("创建")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
}
