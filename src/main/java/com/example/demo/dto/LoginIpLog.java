package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class LoginIpLog {
    @ApiModelProperty("主键id")
    private int id ;
    @ApiModelProperty("用户id")
    private int userId ;
    @ApiModelProperty("登录IP")
    private String ip ;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建事假")
    private Date createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("修改时间")
    private Date updatedAt;
}
