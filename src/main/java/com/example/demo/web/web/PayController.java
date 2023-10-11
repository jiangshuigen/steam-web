package com.example.demo.web.web;


import com.alibaba.fastjson.JSON;
import com.example.demo.config.AliPayConstant;
import com.example.demo.config.Constant;
import com.example.demo.config.ResultData;
import com.example.demo.dto.AliPayOrderInfo;
import com.example.demo.dto.Callback;
import com.example.demo.dto.ExchangeDto;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.BeanRecord;
import com.example.demo.entity.User;
import com.example.demo.service.BeanRecordService;
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
import javax.servlet.http.HttpSession;
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

    @Resource
    private BeanRecordService beanrecordservice;

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
            info.setMsg("请登录");
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
    public String callback(Callback callback) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        /**
         * value替换为所需要的值，此处仅作示例
         */
        map.put("money", String.valueOf(callback.getMoney()));
        map.put("name", "平台充值");
        map.put("out_trade_no", callback.getOut_trade_no());
        map.put("pid", AliPayConstant.APP_KEY);
        map.put("trade_no", callback.getTrade_no());
        map.put("trade_status", callback.getTrade_status());
        map.put("type", AliPayConstant.PAY_TYPE_AILIPAY);
        String preSignContent = SignUtils.getPreSignContent(map);
        map.put("sign_type", "MD5");
        map.put("return_type", "json");
        log.info("preSignContent = {}", preSignContent);
        String sgin = null;
        try {
            sgin = Md5Utils.md5(preSignContent + AliPayConstant.APP_SECRET);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        //获取用户信息
        log.info("Callback info is {}", JSON.toJSONString(callback));
        try {
            //校验
            if (sgin.equals(callback.getSign())) {
                log.info("==============签名校验通过==============================");
                //修改状态
//                payservice.updateBeanRecord(callback);
                try {
                    rabbitTemplate.convertAndSend(Constant.ORDER_CALLBACK_DIRECT_EXCHANGE, Constant.ORDER_CALLBACK_DIRECT_ROUTING, callback.getOut_trade_no());
                    log.info("send message is ===={}", JSON.toJSONString(callback.getOut_trade_no()));
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


    /**
     * 通过cdk充值接口
     *
     * @param cdk
     */
    @ApiOperation(value = "CDK充值接口")
    @PostMapping("/payCDK")
    public ResultData<String> paybyCDK(HttpServletRequest request, @RequestParam("cdk") String cdk) {
        try {
            log.info("cdk is ===={}", JSON.toJSONString(cdk));
            //获取session
            HttpSession session = request.getSession();
            UserDto dto = (UserDto) session.getAttribute(Constant.USER_INFO);
            if (ObjectUtils.isEmpty(dto)) {
                return ResultData.fail("403", "未登录");
            }
            int i = payservice.updateUserByTradeNo(dto, cdk);
            if (i > 0) {
                Thread.sleep(800);
                return ResultData.success("充值成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=====cdk===消息推送失败=====" + e.getMessage());
            return ResultData.fail("500", e.getMessage());
        }
        return null;
    }


    /**
     * 金币和银币相互转化 1
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "金币/银币转化")
    @PostMapping("/exchangeCron")
    public ResultData<String> exchangeCron(HttpServletRequest request, @RequestBody ExchangeDto exchangeDto) {
        try {
            //获取session
            HttpSession session = request.getSession();
            UserDto dto = (UserDto) session.getAttribute(Constant.USER_INFO);
            if (ObjectUtils.isEmpty(dto)) {
                return ResultData.fail("403", "未登录");
            }
            return ResultData.success(userservice.exchangeCron(dto.getId(), exchangeDto));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.fail("500", e.getMessage());
        }
    }


    /**
     * 查询订单状态
     *
     * @param orderNo
     * @return
     */
    @ApiOperation(value = "查询订单信息")
    @GetMapping("/getOrderInfo")
    public ResultData<BeanRecord> getOrderInfo(@RequestParam String orderNo) {
        return ResultData.success(beanrecordservice.getOrderInfo(orderNo));
    }
}
