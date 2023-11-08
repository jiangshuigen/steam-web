package com.example.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.config.Constant;
import com.example.demo.config.SubmailConstant;
import com.example.demo.dto.*;
import com.example.demo.entity.BoxAwards;
import com.example.demo.entity.User;
import com.example.demo.entity.UserExchangeDetail;
import com.example.demo.entity.UserMessage;
import com.example.demo.mapper.LuckyBoxMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.mapper.UserMessageMapper;
import com.example.demo.message.NoticeDto;
import com.example.demo.service.UserService;
import com.example.demo.util.CheckSumBuilder;
import com.example.demo.util.CodeUtils;
import com.example.demo.util.Md5Utils;
import com.example.demo.util.OkHttpUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();
    @Resource
    private UserMapper userMapper;

    @Resource
    private UserMessageMapper usermessagemapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private LuckyBoxMapper mapper;

    public int updateUser(User user) {
        return userMapper.updateUser(user);
    }

    public PageInfo<User> getUserListByPage(UserQuery query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<User> list = userMapper.getUserListByPage(query);
        PageInfo<User> userPageInfo = new PageInfo<>(list);
        return userPageInfo;
    }

    public User getUserById(int id) {
        return userMapper.getUserById(id);
    }

    @Override
    public PageInfo<LoginIpLog> getIPList(LoginIpLogQuery query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<LoginIpLog> list = userMapper.getIPList(query);
        PageInfo<LoginIpLog> userPageInfo = new PageInfo<>(list);
        return userPageInfo;
    }

    @Override
    public UserDto getUserInfo(int id) {
        return userMapper.getUserInfo(id);
    }

    @Override
    public int webUpdateUser(UserUpdateDto user) {
        return userMapper.webUpdateUser(user);
    }

    @Override
    public User getLoginUserInfo(HttpServletRequest req) {
        //获取session
        HttpSession session = req.getSession();
        UserDto dto = (UserDto) session.getAttribute(Constant.USER_INFO);
        if (!ObjectUtils.isEmpty(dto)) {
            User user = new User();
            BeanUtils.copyProperties(dto, user);
            return user;
        }
        return null;
    }

    @Override
    public UserDto userLogin(HttpServletRequest request, LoginInfo info) {
        UserDto dto = userMapper.queryUserInfo(info);
        boolean f = ENCODER.matches(info.getPassword(), dto.getPwd());
        if (!f) {
            return null;
        }
        if (!ObjectUtils.isEmpty(dto)) {
            NoticeDto ms = NoticeDto.builder()
                    .userId(dto.getId())
                    .title("欢迎登陆")
                    .remark("尊敬的用户欢迎登陆！")
                    .build();
            try {
                rabbitTemplate.convertAndSend(Constant.USER_DIRECT_EXCHANGE, Constant.DIRECT_ROUTING, ms);
                log.info("send message is ===={}", JSON.toJSONString(ms));
            } catch (Exception e) {
                e.printStackTrace();
                log.info("=====userLogin===消息推送失败=====" + e.getMessage());
            }
            //存个Session
            request.getSession().setAttribute(Constant.USER_INFO, dto);
        }
        return dto;
    }

    @Override
    public String register(HttpServletRequest request, UserRegisterDto user) {
        //手机-用户名查重校验
        List<User> list = userMapper.queryUserByPhone(user.getMobile());
        if (!CollectionUtils.isEmpty(list)) {
            return "手机号码已经注册";
        }
        //用户名校验
        List<User> listUser = userMapper.queryUserByName(user.getName());
        if (!CollectionUtils.isEmpty(listUser)) {
            return "用户名：" + user.getName() + "已经注册";
        }
        if (!StringUtil.isNullOrEmpty(user.getInviteCode())) {
            User usdto = userMapper.queryUserByInviteCode(user.getInviteCode());
            user.setInviterId(usdto.getId());
        }
        //自动生成邀请码
        boolean bl = true;
        do {
            String invitedCode = CodeUtils.getInviteCode();
            User us = userMapper.queryUserByInviteCode(invitedCode);
            if (ObjectUtils.isEmpty(us)) {
                user.setInviteCode(invitedCode);
                bl = false;
            }
        } while (bl);
        //短信验证码校验
        String mobileCode = (String) redisTemplate.opsForValue().get(user.getMobile());
        if (!(mobileCode != null && mobileCode.equals(user.getMobileCode()))) {
            return "手机验证码错误或已过期";
        }
        //图形验证码校验
        String code = (String) request.getSession().getAttribute(Constant.REGISTER_CODE);
        if (!(code != null && code.equals(user.getCode()))) {
            return "图形验证码错误";
        }
        user.setPassword(ENCODER.encode(user.getPassword()));

        int i = userMapper.register(user);
        return i + "";
    }

    @Override
    public String sendCode(String phone) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(Constant.SERVER_URL);
            String curTime = String.valueOf((new Date()).getTime() / 1000L);
            /*
             * 参考计算CheckSum的java代码，在上述文档的参数列表中，有CheckSum的计算文档示例
             */
            String checkSum = CheckSumBuilder.getCheckSum(Constant.APP_SECRET, Constant.NONCE, curTime);
            // 设置请求的header
            httpPost.addHeader("AppKey", Constant.APP_KEY);
            httpPost.addHeader("Nonce", Constant.NONCE);
            httpPost.addHeader("CurTime", curTime);
            httpPost.addHeader("CheckSum", checkSum);
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            // 设置请求的的参数，requestBody参数
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            /*
             * 1.如果是模板短信，请注意参数mobile是有s的，详细参数配置请参考“发送模板短信文档”
             * 2.参数格式是jsonArray的格式，例如 "['13888888888','13666666666']"
             * 3.params是根据你模板里面有几个参数，那里面的参数也是jsonArray格式
             */
//            nvps.add(new BasicNameValuePair("templateid", Constant.TEMPLATEID));
            nvps.add(new BasicNameValuePair("mobile", phone));
            nvps.add(new BasicNameValuePair("params", Constant.PARAMS));

            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            /*
             * 1.打印执行结果，打印结果一般会200、315、403、404、413、414、500
             * 2.具体的code有问题的可以参考官网的Code状态表
             */
//
            String jsonStr = EntityUtils.toString(response.getEntity());
            log.info("==================" + jsonStr);
            Gson gson = new Gson();
            HttpResponseDto responsed = gson.fromJson(jsonStr, HttpResponseDto.class);
//            HttpResponseDto responsed = (HttpResponseDto) JSONObject.parseArray(jsonArray.toJSONString(), HttpResponseDto.class);
            if (responsed.getCode() == 200) {
                redisTemplate.opsForValue().set(phone, responsed.getObj(), 120, TimeUnit.SECONDS);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean repeatCheck(String str, int type) {
        switch (type) {
            case 1:
                List<User> list = userMapper.queryUserByPhone(str);
                if (!CollectionUtils.isEmpty(list)) {
                    return false;
                }
                break;
            case 2:
                List<User> listUser = userMapper.queryUserByName(str);
                if (!CollectionUtils.isEmpty(listUser)) {
                    return false;
                }
                break;
        }
        return true;
    }

    @Override
    public int updateBean(BigDecimal balance, int id) {
        return userMapper.updateBean(balance, id);
    }

    @Override
    public int resetCache() {
        RedisConnection conn = RedisConnectionUtils.getConnection(redisTemplate.getConnectionFactory());
        //缓存清除
        Set keys = redisTemplate.keys("BoxNumb*");
        Long delete = redisTemplate.delete(keys);
        Set key = redisTemplate.keys("OrderNo*");
        Long deletekey = redisTemplate.delete(key);
        //刷新库存的缓存
        List<BoxAwards> list = mapper.getBoxAwardList();
        RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
        redisTemplate.executePipelined(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                list.forEach((e) -> {
                    //主播-幸运
                    connection.set(("BoxNumb|1|" + e.getId() + "|").getBytes(), String.valueOf(e.getLuckOdds()).getBytes());
                    //普通
                    connection.set(("BoxNumb|3|" + e.getId() + "|").getBytes(), String.valueOf(e.getRealOdds()).getBytes());
                    //主播
                    connection.set(("BoxNumb|4|" + e.getId() + "|").getBytes(), String.valueOf(e.getAnchorOdds()).getBytes());
                });
                return null;
            }
        }, serializer);


        return userMapper.resetCache();
    }

    @Override
    public int sendReward(int userId) {
        return userMapper.sendReward(userId);
    }

    @Override
    public int insertUserMessage(NoticeDto ms) {
        return usermessagemapper.insertUserMessage(ms);
    }

    @Override
    public PageInfo<UserMessage> getMessageList(MessageQuery query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<UserMessage> list = usermessagemapper.getMessageList(query);
        PageInfo<UserMessage> userPageInfo = new PageInfo<>(list);
        return userPageInfo;
    }

    @Override
    public int batchList(int[] ids) {
        return usermessagemapper.batchList(ids);
    }

    @Override
    public int updatePwd(int id) {
        String pwd = ENCODER.encode("123456");
        return userMapper.updatePwd(id, pwd);
    }

    @Override
    public int updateSilver(BigDecimal balance, int id) {
        return userMapper.updateSilver(balance, id);
    }

    @Override
    @Transactional
    public int exchangeCron(int id, ExchangeDto exchangeDto) throws Exception {
        User user = userMapper.getUserById(id);
        if (!ObjectUtils.isEmpty(exchangeDto) && exchangeDto.getType() == 1) {
            //判断余额
            if (exchangeDto.getCount().compareTo(user.getBean()) == 1) {
                throw new Exception("金币余额不足");
            }
            //金币转银币
            user.setBean(user.getBean().subtract(exchangeDto.getCount()));
            user.setSilver(user.getSilver().add(exchangeDto.getCount()));
        } else if (!ObjectUtils.isEmpty(exchangeDto) && exchangeDto.getType() == 2) {
            //银币转金币
            if (exchangeDto.getCount().compareTo(user.getSilver()) == 1) {
                throw new Exception("银币余额不足");
            }
            user.setBean(user.getBean().add(exchangeDto.getCount()));
            user.setSilver(user.getSilver().subtract(exchangeDto.getCount()));
        }
        //保存转化记录
        UserExchangeDetail detail = new UserExchangeDetail();
        detail.setUserId(id);
        detail.setBean(exchangeDto.getCount());
        detail.setType(exchangeDto.getType());
        userMapper.exchangeDetail(detail);
        return userMapper.updateUser(user);
    }

    @Override
    public String sendCodeSubmail(String phone) {
        String sgin = "";
        Random random = new Random();
        //
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            char randomChar = SubmailConstant.CHARACTERS.charAt(random.nextInt(SubmailConstant.CHARACTERS.length()));
            stringBuilder.append(randomChar);
        }
        String captcha = stringBuilder.toString();
        TimeStampResp timestamp = JSON.parseObject(OkHttpUtil.builder().url(SubmailConstant.TIMESTAMP_URL).initGet().sync(), TimeStampResp.class);
        try {
            sgin = Md5Utils.md5(SubmailConstant.APPID + SubmailConstant.APP_KEY
                    + "appid=" + SubmailConstant.APPID + "&project=" + SubmailConstant.TEMPLATE + "&sign_version=2&sign_type=md5&to=" + phone + "&timestamp=" + timestamp.getTimestamp()
                    + SubmailConstant.APPID + SubmailConstant.APP_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject vars = new JSONObject();
        vars.put("code", captcha);
        vars.put("time", "2分钟");
        String result = OkHttpUtil.builder().url(SubmailConstant.URL)
                .addParam("appid", SubmailConstant.APPID)
                .addParam("to", phone)
                .addParam("project", SubmailConstant.TEMPLATE)
                .addParam("vars", vars)
//                .addParam("sign_version", "2")
//                .addParam("sign_type", "md5")
//                .addParam("timestamp", timestamp.getTimestamp())
//                .addParam("signature", sgin)
                .addParam("signature", SubmailConstant.APP_KEY)
                .initPost(true)
                .sync();
        log.info("query result is {} ===============", result);
        MessageResponse res = JSON.parseObject(result, MessageResponse.class);
        if (SubmailConstant.SUCCESS.equals(res.getStatus())) {
            redisTemplate.opsForValue().set(phone, captcha, 120, TimeUnit.SECONDS);
        } else {
            log.error("sendMessage error data is =======" + JSON.toJSONString(res));
        }
        return null;
    }

    //获取时间戳
    private static String getTimestamp() {
        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
        HttpGet httpget = new HttpGet(SubmailConstant.TIMESTAMP_URL);
        try {
            HttpResponse response = closeableHttpClient.execute(httpget);
            HttpEntity httpEntity = response.getEntity();
            String jsonStr = EntityUtils.toString(httpEntity, "UTF-8");
            if (jsonStr != null) {
                JSONObject json = JSONObject.parseObject(jsonStr);
                return json.getString("timestamp");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
