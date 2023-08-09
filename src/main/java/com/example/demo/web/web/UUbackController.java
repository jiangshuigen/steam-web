package com.example.demo.web.web;

import com.alibaba.fastjson.JSON;
import com.example.demo.config.ResultData;
import com.example.demo.dto.CallbackDto;
import com.example.demo.dto.CallbackInfo;
import com.example.demo.service.UUPService;
import com.example.demo.util.RSAUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/back")
@Api(value = "回调接口", tags = {"优品回调"})
@Slf4j
public class UUbackController {

    @Resource
    private UUPService uupservice;

    /**
     * 回调接口
     *
     * @param callback
     * @return
     */
    @ApiOperation(value = "接口回调")
    @PostMapping("/callback")
    public ResultData callback(@RequestBody CallbackDto callback) {
        log.info("回调参数==========={}", JSON.toJSON(callback));
        //签名校验
        if (signCheck(callback)) {
            CallbackInfo info = JSON.parseObject(callback.getCallBackInfo(), CallbackInfo.class);
            uupservice.callback(info);
        } else {
            log.info("验签失败");
        }
        return ResultData.success(1);
    }

    private boolean signCheck(CallbackDto callback) {
        String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkQgPSzuzq8qIKW9iTZykyAlzYb+0N3m+1IxqXV9j1Ior60o7zI29a2vcjE/BbPpo9mPFexkcMzGREKrODHoIqkdjYqBLG2+VRZIynv4XD1AQyFO4vDhLIet7UGhMvSlClYS1nrlhyXSFpb/Z/7T146CT/YZfcY3aUtLtGIhBUMYBUItl8V9pnUqOMknGL1TXATIxv2+a0cdnOWhTzZdqIExzTFzaHRho8zobONaZLHR+Fx+zXeZQ0Gs4Kxst4xMs2MSwc7JqeQtAor57ZxMqnmjnbMcexiT8kF+720pZC/74sIzar0y7u8GlVTGBYhZ5njfevYVGgoQN49QV4H6vBwIDAQAB";
        Map<String, Object> params = new HashMap<>();
        params.put("messageNo", callback.getMessageNo());
        //注意接收到的callBackInfo是含有双引号转译符"\" 文档上无法体现只需要在验证签名是直接把callBackInfo值当成字符串即可以
        params.put("callBackInfo", callback.getCallBackInfo());
        // 第一步：检查参数是否已经排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        // 第二步：把所有参数名和参数值串在一起
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : keys) {
            Object value = params.get(key);
            if (!StringUtils.isEmpty(value)) {
                stringBuilder.append(key).append(value);
            }
        }
        log.info("stringBuilder:{}", stringBuilder);
        try {
            return RSAUtils.verifyByPublicKey(stringBuilder.toString().getBytes(), pubKey, callback.getSign());
        } catch (Exception e) {
            log.error("校验签名异常====={}", e.getMessage());
            e.printStackTrace();
        }
        return false;
    }


}
