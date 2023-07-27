package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class UserMessage {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("用户id")
    private int userId;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("内容")
    private String remark;
    @ApiModelProperty("状态0-未读 1-已读")
    private int status;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created_at;
}
