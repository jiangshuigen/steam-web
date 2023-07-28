package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class PromotionQuery {
    @ApiModelProperty("用户id")
    private int userId;
    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date begin;
    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date end;
    @ApiModelProperty("排序 0-正序 1-倒序")
    private int sort;
}
