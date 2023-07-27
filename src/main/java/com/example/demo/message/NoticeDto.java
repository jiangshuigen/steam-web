package com.example.demo.message;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;

@Data
@Builder
public class NoticeDto implements Serializable {
    @ApiModelProperty("用户id")
    private int userId;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("内容")
    private String remark;

    @Tolerate
    public NoticeDto() {
    }
}
