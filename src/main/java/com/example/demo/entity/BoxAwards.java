package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BoxAwards {
    @ApiModelProperty("主键id")
    private int id;
    @ApiModelProperty("boxId")
    private int boxId;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("饰品唯一英文名称")
    private String hashName;
    @ApiModelProperty("封面")
    private String cover;
    @ApiModelProperty("外观")
    private int dura;
    @ApiModelProperty("品质")
    private int lv;
    @ApiModelProperty("投放数量")
    private int realOdds;
    @ApiModelProperty("投放数量(主播)")
    private int anchorOdds;
    @ApiModelProperty("投放数量(幸运)")
    private int luckOdds;
    @ApiModelProperty("(剩余)投放数量")
    private int realOddsLeave;
    @ApiModelProperty("(剩余)投放数量(主播)")
    private int anchorOddsLeave;
    @ApiModelProperty("(剩余)投放数量(幸运)")
    private int luckOddsLeave;
    @ApiModelProperty("金豆")
    private BigDecimal bean;
    @ApiModelProperty("装备类型")
    private int type;
    @ApiModelProperty("幸运开箱 0:否 1:是")
    private int isLuckyBox;
    @ApiModelProperty("库存")
    private int shopInventory;
    @ApiModelProperty("幸运区间")
    private String luckInterval;
    @ApiModelProperty("幸运区间主播")
    private String luckIntervalAnchor;
    @ApiModelProperty("剩余额度")
    private BigDecimal leaveBean;
    @ApiModelProperty("剩余额度(主播)")
    private BigDecimal leaveBeanAnchor;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;


}
