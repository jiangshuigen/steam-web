package com.example.demo.service.pay.impl;

import com.alibaba.fastjson.JSON;
import com.example.demo.config.AliPayConstant;
import com.example.demo.config.Constant;
import com.example.demo.dto.AliPayOrderInfo;
import com.example.demo.dto.Callback;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.BeanRecord;
import com.example.demo.entity.User;
import com.example.demo.service.BeanRecordService;
import com.example.demo.service.CardService;
import com.example.demo.service.pay.PayService;
import com.example.demo.util.EncodeUtils;
import com.example.demo.util.HttpUtils;
import com.example.demo.util.Md5Utils;
import com.example.demo.util.SignUtils;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

@Service
@Slf4j
public class PayServiceImpl implements PayService {

    @Value("${callback.url}")
    private String callbackUrl;
    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private BeanRecordService beanrecordservice;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private CardService cardservice;

    @Override
    public AliPayOrderInfo getOrderNumber(User usr, int count, HttpServletRequest request) {
        Object ob = redisTemplate.opsForValue().get("PayOrderN-");
        String lock = "";
        if (!ObjectUtils.isEmpty(ob)) {
            lock = (String) ob;
        }
        if (StringUtil.isNullOrEmpty(lock)) {
            lock = "1";
        } else {
            int num = Integer.parseInt(lock) + 1;
            if ((num < 100)) {
                lock = String.valueOf(num);
            } else {
                lock = "1";
            }
        }
        redisTemplate.opsForValue().set("PayOrderN-", lock);
        DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String localDate = LocalDateTime.now().format(ofPattern);
        String orderNum = localDate + lock;
        // 1、校验参数 @NotNull注解参数完成此步骤
        // 2、生成签名、参数
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        /**
         * value替换为所需要的值，此处仅作示例
         */
        map.put("app_key", AliPayConstant.APP_KEY);
        map.put("api_domain", AliPayConstant.API_DOMAIN);
        map.put("goods_id", "805");
        map.put("goods_num", "1");
        map.put("order_id", orderNum);
        map.put("is_mobile", "1");
        map.put("key", AliPayConstant.APP_SECRET);
        String preSignContent = SignUtils.getPreSignContent(map);
        log.info("文档比对   = app_key=app_key&api_domain=api_domain&goods_id=goods_id&goods_num=goods_num&order_id=order_id&is_mobile=is_mobile&key=key");
        log.info("preSignContent = {}", preSignContent);
        String md5 = null;
        try {
            md5 = Md5Utils.md5(preSignContent);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        map.put("contact", usr.getMobile());
        map.put("callback_url", callbackUrl);
        map.put("ip_address", this.getRemoteIP(request));
        map.put("pay_type", "0");
        map.put("sign", md5);
        map.put("discount_price", String.valueOf(count * 100));

        // 3、发送请求
        String result = HttpUtils.sendPost(AliPayConstant.PAY_URL, map);
        log.info("result is {}", result);
        AliPayOrderInfo res = JSON.parseObject(result, AliPayOrderInfo.class);
        //4、处理订单
        if (AliPayConstant.SUCCESS_STATUS == res.getResults().getStatus()) {
            //业务：创建订单记录 -状态未支付
            BeanRecord record = new BeanRecord();
            record.setUserId(usr.getId());
            record.setInviterId(usr.getInviterId());
            record.setBean(BigDecimal.valueOf(count));
            record.setPrice(BigDecimal.valueOf(count));
            record.setFinallyPrice(BigDecimal.valueOf(count));
            record.setCode(orderNum);
            record.setTradeNo("");
            record.setStatus(0);
            record.setIsBenefit(0);
            record.setIsPayApi(1);
            int i = beanrecordservice.insertBeanReacord(record);
        } else {
            log.error("错误信息：{}", EncodeUtils.decodeUnicode(res.getResults().getError()));
        }
        return res;
    }

    @Override
    public int updateBeanRecord(Callback callback) {
        return beanrecordservice.updateBeanRecordsStatus(callback);
    }

    @Override
    @Transactional
    public int updateUserByTradeNo(UserDto dto, String cdk) throws Exception {
        //幂等性校验
        BeanRecord record = beanrecordservice.queryBeanRecordsByTradeNo(cdk);
        if (!ObjectUtils.isEmpty(record) && record.getStatus() == 1) {
            throw new Exception("CDK:{}已经使用，请使用其他CDK");
        }
        int i = beanrecordservice.updateUserByTradeNo(dto, cdk);
        //修改cdk使用状态
        cardservice.updateCardByNumber(cdk);
        if (i > 0) {
            rabbitTemplate.convertAndSend(Constant.ORDER_CALLBACK_DIRECT_EXCHANGE,
                    Constant.ORDER_CALLBACK_DIRECT_ROUTING, record.getCode());
        }
        return i;
    }

    private String getRemoteIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
