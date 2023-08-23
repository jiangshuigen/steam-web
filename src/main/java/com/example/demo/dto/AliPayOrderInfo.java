package com.example.demo.dto;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AliPayOrderInfo {
    @ApiModelProperty("调用接口异常提示")
    @JSONField(name = "err_msg")
    private String errMsg;

    @ApiModelProperty("调用接口异常状态 (0：正常)")
    @JSONField(name = "err_no")
    private int errNo;

    @ApiModelProperty("返回数据")
    @JSONField(name = "results")
    private Results results;

    @Data
    public class Results{
        @ApiModelProperty("错误信息")
        @JSONField(name = "error")
        private String error;
        @ApiModelProperty("订单状态")
        @JSONField(name = "status")
        private int status;
        @ApiModelProperty("二维码")
        @JSONField(name = "url")
        private int url;

    }
}
