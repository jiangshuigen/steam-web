package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class RoomDto {
    @ApiModelProperty("用户id")
    private int userId;
    @ApiModelProperty("房间名称")
    private String name;
    @ApiModelProperty("说明")
    private String describe;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("结束时间")
    private Date endTime;
    @ApiModelProperty("房间人数")
    private int peopleNumber;
    @ApiModelProperty("密码")
    private String password;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("累计充值开始时间")
    private Date payStartTime;
    @ApiModelProperty("累计充值最小金额")
    private BigDecimal minRecharge;
    @ApiModelProperty("奖品")
    private List<Integer> awards;
}
