package com.example.demo.web.web;


import com.alibaba.fastjson.JSON;
import com.example.demo.config.AliPayConstant;
import com.example.demo.config.Constant;
import com.example.demo.dto.AliPayOrderInfo;
import com.example.demo.dto.Callback;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.service.pay.PayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/web/pay")
@Api(value = "支付", tags = {"支付"})
@Slf4j
public class PayController {

    @Resource
    private PayService payservice;
    @Resource
    private UserService userservice;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 创建订单
     *
     * @param request
     * @param count
     * @return
     */
    @ApiOperation(value = "创建订单")
    @PostMapping("/order")
    public AliPayOrderInfo getOrderNumber(HttpServletRequest request, @RequestParam("count") int count) {
        //获取用户信息
        User usr = userservice.getLoginUserInfo(request);
        if (!ObjectUtils.isEmpty(usr)) {
            String ip = request.getRemoteAddr();
            System.out.println(ip);
            return payservice.getOrderNumber(usr, count,request);
        } else {
            AliPayOrderInfo info = new AliPayOrderInfo();
            info.setErrMsg("请登录");
            return info;
        }
    }


    /**
     * 支付成功回调
     *
     * @param callback
     * @return
     */
    @ApiOperation(value = "成功回调")
    @PostMapping("/callback")
    public String callback(@RequestBody Callback callback) {
        String key = "api_order_id=api_order_id" +
                "&mch_id=" + callback.getMch_id() + "" +
                "&order_id=" + callback.getOrder_id() + "" +
                "&pay_type=" + callback.getPay_type() + "" +
                "&success_at=" + callback.getSuccess_at() + "" +
                "&total_amount=" + callback.getTotal_amount() + "" +
                "&key=" + AliPayConstant.APP_SECRET;
        //获取用户信息
        log.info("Callback info is {}", JSON.toJSONString(callback));
        try {
            String sgin = DigestUtils.md5DigestAsHex(key.getBytes("utf-8")).toLowerCase();
            //校验
            if (sgin.equals(callback.getSign())) {
                log.info("==============签名校验通过==============================");
                //修改状态
                payservice.updateBeanRecord(callback);
                try {
                    rabbitTemplate.convertAndSend(Constant.ORDER_CALLBACK_DIRECT_EXCHANGE, Constant.ORDER_CALLBACK_DIRECT_ROUTING, callback.getApi_order_id());
                    log.info("send message is ===={}", JSON.toJSONString(callback.getApi_order_id()));
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("=====callback===消息推送失败=====" + e.getMessage());
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "success";
    }
}
