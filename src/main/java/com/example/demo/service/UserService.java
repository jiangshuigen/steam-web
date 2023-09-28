package com.example.demo.service;

import com.example.demo.config.ResultData;
import com.example.demo.dto.*;
import com.example.demo.entity.User;
import com.example.demo.entity.UserMessage;
import com.example.demo.message.NoticeDto;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

public interface UserService {

    PageInfo<User> getUserListByPage(UserQuery query);

    int updateUser(User user);

    User getUserById(int id);

    PageInfo<LoginIpLog> getIPList(LoginIpLogQuery query);


    UserDto getUserInfo(int id);

    int webUpdateUser(UserUpdateDto user);

    /**
     * 获取登录用户信息
     *
     * @return
     */
    User getLoginUserInfo(HttpServletRequest req);

    UserDto userLogin(HttpServletRequest request, LoginInfo info);

    String register(HttpServletRequest request, UserRegisterDto user);

    String sendCode(String phone);

    boolean repeatCheck(String str, int type);

    int updateBean(BigDecimal balance, int id);

    int resetCache();

    int sendReward(int userId);

    int insertUserMessage(NoticeDto ms);

    PageInfo<UserMessage> getMessageList(MessageQuery query);

    int batchList(int[] ids);

    int updatePwd(int id);

    int updateSilver(BigDecimal balance, int id);

    int exchangeCron(int id, ExchangeDto exchangeDto) throws Exception;
}
