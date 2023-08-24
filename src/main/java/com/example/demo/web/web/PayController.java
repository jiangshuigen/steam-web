package com.example.demo.web.web;


import com.alibaba.fastjson.JSON;
import com.example.demo.config.AliPayConstant;
import com.example.demo.config.Constant;
import com.example.demo.dto.AliPayOrderInfo;
import com.example.demo.dto.Callback;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.service.pay.PayService;
import com.example.demo.util.Md5Utils;
import com.example.demo.util.QRCodeUtils;
import com.example.demo.util.SignUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;

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
            return payservice.getOrderNumber(usr, count, request);
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
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        /**
         * value替换为所需要的值，此处仅作示例
         */
        map.put("api_order_id", callback.getApi_order_id());
        map.put("mch_id", String.valueOf(callback.getMch_id()));
        map.put("order_id", callback.getOrder_id());
        map.put("pay_type", callback.getPay_type());
        map.put("success_at", callback.getSuccess_at());
        map.put("total_amount", String.valueOf(callback.getTotal_amount()));
        map.put("key", AliPayConstant.APP_SECRET);
        //获取用户信息
        log.info("Callback info is {}", JSON.toJSONString(callback));
        try {
            String preSignContent = SignUtils.getPreSignContent(map);
            String sgin = Md5Utils.md5(preSignContent);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    @ApiOperation(value = "手动补偿接口")
    @PostMapping("/retry")
    public void callback(@RequestParam("orderNo") String orderNo) {
        try {
            rabbitTemplate.convertAndSend(Constant.ORDER_CALLBACK_DIRECT_EXCHANGE, Constant.ORDER_CALLBACK_DIRECT_ROUTING, orderNo);
            log.info("send message is ===={}", JSON.toJSONString(orderNo));
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=====callback===消息推送失败=====" + e.getMessage());
        }
    }

    /**
     * 生成二维码
     *
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/addOneQRCode")
    @ApiOperation(value = "成功回调url生成二维码")
    public String addOneQRCode(HttpServletRequest request, HttpServletResponse response, @RequestParam("url") String url) {
        BufferedImage image = null;
        //这里可以先通过查库将需要生成的数据拼接到content中，然后作为二维码的标题
        String content = "支付二维码";
        try {
            image = QRCodeUtils.generateQrCodeBack(url, content);
            ServletOutputStream os = response.getOutputStream();
            ImageIO.write(image, "jpg", os);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "fail";
    }


}
