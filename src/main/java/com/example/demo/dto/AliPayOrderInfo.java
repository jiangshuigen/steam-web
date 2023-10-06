package com.example.demo.dto;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AliPayOrderInfo {
    @ApiModelProperty("调用接口异常提示")
    @JSONField(name = "msg")
    private String msg;

    @ApiModelProperty("调用接口异常状态 (1：正常)")
    @JSONField(name = "code")
    private int code;

    @ApiModelProperty("返回数据")
    @JSONField(name = "data")
    private Results data;

    @Data
    public class Results{
        @ApiModelProperty("二维码")
        @JSONField(name = "payurl")
        private String payurl;

    }
}
