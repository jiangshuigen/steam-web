package com.example.demo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel
public class CreateOrderDTO implements Serializable {
    private static final long serialVersionUID = -6374172750009654518L;

    public interface Insert {
    }

    /**
     * 商户API对接服务---API Key
     */
    @ApiModelProperty(value = "商户API对接服务---API Key",required = true)
    @NotNull(message = "app_key 不能为空",groups = Insert.class)
    private String app_key;

    /**
     * 发起支付网站域名
     */
    @ApiModelProperty(value = "发起支付网站域名",required = true)
    @NotNull(message = "api_domain 不能为空",groups = Insert.class)
    private String api_domain;

    /**
     * 商品ID，商品管理---我的商品中获取
     */
    @ApiModelProperty(value = "商品ID，商品管理---我的商品中获取",required = true)
    @NotNull(message = "goods_id 不能为空",groups = Insert.class)
    private String goods_id;

    /**
     * 购买的商品数量
     */
    @ApiModelProperty(value = "购买的商品数量",required = true)
    @NotNull(message = "goods_num 不能为空",groups = Insert.class)
    private String goods_num;

    /**
     * 对接商户内部订单号，非本平台订单号
     */
    @ApiModelProperty(value = "对接商户内部订单号，非本平台订单号",required = true)
    @NotNull(message = "order_id 不能为空",groups = Insert.class)
    private String order_id;

    /**
     * 是否移动端0否1是
     */
    @ApiModelProperty(value = "是否移动端0否1是",required = true)
    @NotNull(message = "is_mobile 不能为空",groups = Insert.class)
    private String is_mobile;

    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式")
    private String contact;

    /**
     * 付款成功回调通知地址,此地址必须是商户后台API对接管理中已对接的网址之一
     */
    @ApiModelProperty(value = "付款成功回调通知地址,此地址必须是商户后台API对接管理中已对接的网址之一",required = true)
    @NotNull(message = "callback_url 不能为空",groups = Insert.class)
    private String callback_url;

    /**
     * 客户下单ip地址
     */
    @ApiModelProperty(value = "客户下单ip地址",required = true)
    @NotNull(message = "ip_address 不能为空",groups = Insert.class)
    private String ip_address;

    /**
     * 支付渠道(0：支付宝，1：微信)
     */
    @ApiModelProperty(value = "支付渠道(0：支付宝，1：微信)",required = true)
    @NotNull(message = "pay_type 不能为空",groups = Insert.class)
    private String pay_type;

    /**
     * 签名串
     * 1 将以下参数按照URL键值对的格式app_key=app_key&api_domain=api_domain&goods_id=goods_id&goods_num=goods_num&order_id=order_id&is_mobile=is_mobile&key=key拼接为字符串，参数的顺序要严格按文档排列，不能调换签名串中参数顺序，(这里的key值是API对接服务里的ApI Secret，需要商户自行获取)
     * 2 将得到的字符串通过md5加密后，全部转换为小写
     */
    @ApiModelProperty(value = "签名串",required = true)
    @NotNull(message = "sign 不能为空",groups = Insert.class)
    private String sign;

    /**
     * 折扣价格，在畅想平台创建商品时选择任意金额，此处可自定义金额，总支付金额即此处传入金额，如不清楚请联系客服
     */
    @ApiModelProperty(value = "折扣价格，在畅想平台创建商品时选择任意金额，此处可自定义金额，总支付金额即此处传入金额，如不清楚请联系客服")
    private String discount_price;

}
