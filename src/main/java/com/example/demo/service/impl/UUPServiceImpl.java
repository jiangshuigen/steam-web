package com.example.demo.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.mapper.BoxRecordMapper;
import com.example.demo.mapper.LuckyBoxMapper;
import com.example.demo.mapper.UUPMapper;
import com.example.demo.service.UUPService;
import com.example.demo.service.UserService;
import com.example.demo.util.JacksonUtils;
import com.example.demo.util.JsonUtil;
import com.example.demo.util.OkHttpUtil;
import com.example.demo.util.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class UUPServiceImpl implements UUPService {
    //私钥
    public static final String pvtKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCjT3Z35bj+moK1oZJeI431i9r2CgveB3+e+KCrzvSUnux8Cq9cJMy9Y3adgKqIIzWtmvpl24U0vwrKNuXPaG/Ypu684hyTTzCTVBA1bp6uU1EAbIDG0xMC7W8WEfp/JE81vuZmV88YoXw9w9meS8aWMrYqrAY1fUn8ZdRE8t3bBvW1mHYU2Q3k8E+w7MuVf0VKKK23avKHsdDwgwFdv9AhC+EnRFVsrlOgAyKR6LSQ3oboqpimts/yoP4TmIwFoi9kWke96f70i1I7U87cm6J/O/kQfmxXM8Gr9VC1J4TUFNjCkT7zI5cWO3O79k6yy4hEU7aQRS40V1OBMA6ViUWzAgMBAAECggEAGmdqnBAETQXmRD+PTrEo7p5iFI2jRY6oz6cbMywO10iqq5N+rsM0ZxqBvw1ihQhMUXlRKs7HRrPspq0hktsr2jeDOf7E5/xRukBJLppoBX9rBUqFt5/A3yRMZaEagxv3wpTO45Um4rCZSdM0iCdwQDCKy3NsvZE4ORqtq0gpHpRKxxAba7v7FmGFSJF/92kK35Jorydak+ro1PD/U2oezKNTAGa4/t+SJh/fNEXWn4VPnHLVal3JEVo+TYAAn1rQVlcGMJKH2RS7hQLYYAIXcSTaUXWMb2HS0luhVG5tWW+GKa2tT6hopEJIaX6agPZOO2F5cXsxcwPksueXSoGVYQKBgQDp0rX6AX/Z+053OoG4zO0Us9q9XDPUKGLw1hBuNLbEIG2dFo47Ia7uuvCM2zQPU0Kqk2kMjmm1hczDpMKrp+OiHsVZZ31ZPqhLip/JjrSmxNRMPxERC+ozUcnV9Jr1aIWmeIPb64Q8pi9j/vLXfxcRCCEASa9WZEIz2Fd9hOmQZQKBgQCyzK80kM1L0oU/hvkfIaYjn1uPGTWH0wwUMi1G/3Hx18jkl2LcmRG7J1GoptTadON9+DqkCF/2NLKbFFMSJsUIwApWCtWWpN02lOkK7+iK96rzHcaz/MLJTGM/m1kzhMLons9FhbJJmyy1rM1AIKWzA8Rs83WXTOjF0CvqBRpANwKBgFOxilV8Z+j8XO+sT3Z4U3wPjIIvGJYeBpwx4xuvXqQA/3s08aAUxGrLGwMwCwhZQwmPThCigQ/qXorA1LVgmMgUv4rq0iE5nj+71MnufJcc6OiJwnAZlbDc26Prs/OXgA9AH+J+AI085sWiIgYkhXshL77MoSgJJvErxNU21+tJAoGAY8mYft3Rtd9oPmw13QoKjxcBInwPRB6lPyBB5L3r+e1UUT+sDTHgk3L+QAHcCY/y5vsCv0ltRRtkEujk89xvWtLS8fJaAhlA3JV++nuqbOSMo+KaYxlOHMplGbq5recKYIlBZKuZNTtFWJ94bbHKyH3xwlyd5DMoNj5YQ+NVI0ECgYAyVozsS0TF+xlu5RDR8i2c1sfS2X28UQXzECVYQ9yzyOVcvOIPQNU1uRUAPuoJeMr7wL8u7V79cUsYwdQGRXLMI56zVjlSTYy0RRMvVe7GmvhftHQnVFz//wp7HFnNxuZhiIeS01x89HVmyBeB021X0KN6VbUsHcVpFkFEFyJQ6A==";
    public static final String appKey = "89784";

    public static int number = 0;
    @Resource
    private UUPMapper uupmapper;
    @Resource
    private LuckyBoxMapper mapper;
    @Resource
    private BoxRecordMapper boxrecordmapper;
    @Resource
    private UserService userservice;

    @Override
    public UUResponse getTemplateList() {
        //组装参数
        Map<String, Object> params = this.getBaseMap();
        //采用私钥签名
        try {
            String sign = this.sign(params);
            String result = OkHttpUtil.builder().url("http://gw-openapi.youpin898.com/open/v1/api/templateQuery")
                    .addParam("sign", sign)
                    .addParam("timestamp", String.valueOf(params.get("timestamp")))
                    .addParam("appKey", appKey)
                    .initPost(true)
                    .sync();
            UUResponse res = JSON.parseObject(result, UUResponse.class);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String importData(MultipartFile file) {
        try {
            if (file == null) {
                return "数据为空";
            }
            String jsonStr = JsonUtil.convertStreamToString(file.getInputStream());
            List<UUbaseData> myObjectList = JSON.parseArray(jsonStr, UUbaseData.class);
            log.info("导入数据size：========,{}", myObjectList.size());
            uupmapper.insertBaseData(myObjectList);
        } catch (IOException e) {
            log.error("==数据解析异常：" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<UUAwardDto> getUUList(UUQuery query) {
        List<UUAwardDto> listReturn = new ArrayList<>();
        Map<String, Object> params = this.getBaseMap();
        params.put("page", query.getPageNo());
        params.put("pageSize", query.getPageSize());
        params.put("typeId", query.getUuTypeId());
        String sign = this.sign(params);
        params.put("sign", sign);
        String result = OkHttpUtil.builder().url("http://gw-openapi.youpin898.com/open/v1/api/queryTemplateSaleByCategory")
                .addParam("sign", sign)
                .addParam("timestamp", String.valueOf(params.get("timestamp")))
                .addParam("appKey", appKey)
                .addParam("page", query.getPageNo())
                .addParam("pageSize", query.getPageSize())
                .addParam("typeId", query.getUuTypeId())
                .initPost(true)
                .sync();
        UUResponse res = JSON.parseObject(result, UUResponse.class);
        if (res.getCode() == 0) {
            log.info("data is =======" + JSON.toJSONString(res));
            String json = JSONUtils.toJSONString(res.getData());
            JSONObject jsonObject = JSONObject.parseObject(json);
            Object ob = jsonObject.get("saleTemplateByCategoryResponseList");
            List<UUAwardDto> list = JSON.parseArray(JSON.toJSONString(ob), UUAwardDto.class);
            if (!CollectionUtils.isEmpty(list)) {
                try {
                    //入库
                    int dataNumber = uupmapper.insertBaseAwardData(list);
                    log.info("=================插入成功：{}条=========", dataNumber);
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                listReturn.addAll(list);
            }
            boolean flag = (boolean) jsonObject.get("newPageIsHaveContent");
            if (flag) {
                UUQuery queryInfo = query;
                queryInfo.setPageNo(queryInfo.getPageNo() + 1);
                List<UUAwardDto> listIn = getUUList(queryInfo);
                listReturn.addAll(listIn);
            }
        } else {
            log.info("error info is =======" + JSON.toJSONString(res));
        }
        return listReturn;
    }

    @Override
    public List<UUAwardDto> getWebUUList(BasePage query) {
        List<UUAwardDto> listReturn = new ArrayList<>();
        List<AwardTypes> listType = mapper.getTypeList();
        List<AwardTypes> reList = listType.stream().filter(e -> e.getUuWeaponId() <= 0).collect(Collectors.toList());
        reList.forEach(e -> {
            //循环所有类型查询数据
            log.info("查询类型的数据列表 =======" + JSON.toJSONString(e));
            UUQuery queryDto = new UUQuery();
            queryDto.setPageNo(query.getPageNo());
            queryDto.setPageSize(query.getPageSize());
            queryDto.setUuTypeId(e.getUuTypeId());
            List<UUAwardDto> list = this.getUUList(queryDto);
            listReturn.addAll(list);
        });
        log.info("获取数据总量========={}", listReturn.size());
        return listReturn;
    }

    @Override
    public List<UUAward> getUUAwardList(String templateHashName) {
        Map<String, Object> params = this.getBaseMap();
        params.put("templateHashName", templateHashName);
        params.put("pageSize", 10);
        params.put("page", 1);
        String sign = this.sign(params);
        String result = OkHttpUtil.builder().url("http://gw-openapi.youpin898.com/open/v1/api/goodsQuery")
                .addParam("sign", sign)
                .addParam("timestamp", String.valueOf(params.get("timestamp")))
                .addParam("appKey", appKey)
                .addParam("templateHashName", templateHashName)
                .addParam("pageSize", 10)
                .addParam("page", 1)
                .initPost(true)
                .sync();
        UUResponse res = JSON.parseObject(result, UUResponse.class);
        if (res.getCode() == 0) {
            List<UUAward> list = JSON.parseArray(JSON.toJSONString(res.getData()), UUAward.class);
            return list;
        } else {
            log.error("getUUAwardList error data is =======" + JSON.toJSONString(res));
        }
        return null;
    }

    @Override
    @Transactional
    public UUResponse buyAwards(UUOrder dto) {
        //查询订单信息
        BoxRecords record = boxrecordmapper.getRecordById(dto.getRecordId());
        Map<String, Object> params = this.getBaseMap();
        params.put("merchantOrderNo", record.getCode());
        params.put("tradeLinks", record.getSteamUrl());
        params.put("commodityId", dto.getCommodityId());
        params.put("purchasePrice", dto.getPurchasePrice());
        String sign = this.sign(params);
        String result = OkHttpUtil.builder().url("http://gw-openapi.youpin898.com/open/v1/api/byGoodsIdCreateOrder")
                .addParam("sign", sign)
                .addParam("timestamp", String.valueOf(params.get("timestamp")))
                .addParam("appKey", appKey)
                .addParam("merchantOrderNo", record.getCode())
                .addParam("tradeLinks", record.getSteamUrl())
//                .addParam("tradeLinks", "https://steamcommunity.com/tradeoffer/new/?partner=467543820&token=KcgFJVAW")
                .addParam("commodityId", dto.getCommodityId())
                .addParam("purchasePrice", dto.getPurchasePrice())
                .initPost(true)
                .sync();
        UUResponse res = JSON.parseObject(result, UUResponse.class);
        log.info("buyAwards  data is =======" + JSON.toJSONString(res));
        if (res.getCode() == 0) {
            String json = JSONUtils.toJSONString(res.getData());
            JSONObject jsonObject = JSONObject.parseObject(json);
            String orderNo = (String) jsonObject.get("orderNo");
            BigDecimal payAmount = jsonObject.getBigDecimal("payAmount");
            Integer orderStatus = jsonObject.getInteger("orderStatus");
            //业务处理
            String status = "1"; //待操作
            int i = uupmapper.updateStatusByOrderNo(record.getCode(), status);
            if (i > 1) {
                log.error("订单为{} 有问题=======", record.getCode());
            }
            //添加发货记录
            DeliveryRecord deliveryrecord = new DeliveryRecord();
            deliveryrecord.setUserId(record.getUserId());
            deliveryrecord.setRecordId(record.getId());
            deliveryrecord.setRecordCode(record.getCode());
            deliveryrecord.setTradeNo(orderNo);
            deliveryrecord.setPrice(payAmount);
            deliveryrecord.setDelivery(1);
            deliveryrecord.setZbtStatus(9);//发货中
            deliveryrecord.setOrderId(orderNo);
            uupmapper.addDeliveryRecords(deliveryrecord);
        } else {
            log.error("buyAwards error data is =======" + JSON.toJSONString(res));
        }
        return null;
    }

    @Override
    @Transactional
    public void callback(CallbackInfo info) {
        BoxRecords red = boxrecordmapper.getRecordByOrderNo(info.getMerchantOrderNo());
        switch (info.getOrderStatus()) {
            case 340:
                log.info("平台订单号为:{}订单号为:{}收货成功，===完成订单", info.getMerchantOrderNo(), info.getOrderNo());
                //修改订单状态
                uupmapper.updateStatus(info.getOrderNo(), "10");
                String status = "1"; //领取成功
                int i = uupmapper.updateStatusByOrderNo(info.getMerchantOrderNo(), status);
                break;
            case 280:
                log.info("平台订单号为:{}订单号为:{}取消订单，===取消订单", info.getMerchantOrderNo(), info.getOrderNo());
                String status_str = "1407,1409,1410,1412";//因代购对象原因取消的订单
                if (status_str.contains(String.valueOf(info.getOrderSubStatus()))) {
                    log.info("因代购对象原因取消的订单，将会扣除该订单金额的2%=====");
                    //兑换
                    int sta = boxrecordmapper.exchangeOrderNo(info.getMerchantOrderNo());
                    if (sta > 0) {
                        User u = userservice.getUserById(red.getUserId());
                        BigDecimal balance = u.getBean().add(red.getBean().multiply(new BigDecimal(0.97)).setScale(2, BigDecimal.ROUND_DOWN));
                        userservice.updateBean(balance, u.getId());
                    }
                } else {
                    log.info("正常取消订单=========");
                    uupmapper.updateStatus(info.getOrderNo(), "9");
                    uupmapper.updateStatusByOrderNo(info.getMerchantOrderNo(), "0");
                }
                break;
        }

    }

    @Override
    public List<UUawardsDto> getAllUUAwardList() {
        return uupmapper.queryAllUUAwardList();
    }

    @Override
    public void updateAwardsBean(UUawardsDto uUawardsDto) {
        uupmapper.updateAwardsBean(uUawardsDto);
    }


    /**
     * 基础参数
     *
     * @return
     */
    private Map<String, Object> getBaseMap() {
        //组装参数
        Map<String, Object> params = new HashMap<>();
        String timeStr1 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        params.put("timestamp", timeStr1);
        params.put("appKey", appKey);
        return params;
    }

    /**
     * 签名
     */
    private String sign(Map<String, Object> params) {
        // 第一步：检查参数是否已经排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        // 第二步：把所有参数名和参数值串在一起
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : keys) {
            Object value = params.get(key);
            if (!StringUtils.isEmpty(value)) {
                stringBuilder.append(key).append(JacksonUtils.writeValueAsString(value));
            }
        }
        try {
            return RSAUtils.signByPrivateKey(stringBuilder.toString().getBytes(), pvtKey);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("============签名失败{}===================" + e.getMessage());
        }
        return null;
    }
}
