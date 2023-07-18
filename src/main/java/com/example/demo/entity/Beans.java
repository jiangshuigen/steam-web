package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Beans {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("RMB")
    private BigDecimal price;
    @ApiModelProperty("金豆")
    private BigDecimal bean;
    @ApiModelProperty("启用禁言")
    private int isPutaway;
    @ApiModelProperty("卡密链接")
    private String cardLink;
    @ApiModelProperty("商品id")
    private int productId;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("修改时间")
    private Date updatedAt;

}
