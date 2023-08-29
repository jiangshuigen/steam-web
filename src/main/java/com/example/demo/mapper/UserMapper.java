package com.example.demo.mapper;

import com.example.demo.dto.*;
import com.example.demo.entity.Dashboard;
import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface UserMapper {

    Integer insertUser(User user);

    Integer batchInsertUser(List<User> users);

    User deleteUser(Integer id);

    int updateUser(User user);

    User selectUser(Integer id);

    List<User> selectAllUser();

    Dashboard getDashboard();

    List<User> getUserListByPage(UserQuery query);

    User getUserById(int id);

    List<LoginIpLog> getIPList(LoginIpLogQuery query);


    UserDto getUserInfo(int id);

    int webUpdateUser(UserUpdateDto user);

    UserDto queryUserInfo(LoginInfo info);

    List<User> queryUserByPhone(String mobile);

    List<User> queryUserByName(String name);

    int register(UserRegisterDto user);

    User queryUserByInviteCode(String inviteCode);

    int updateBean(@Param("balance") BigDecimal balance, @Param("id") int id);

    int resetCache();

    int sendReward(int userId);

    int updatePwd(@Param("id") int id, @Param("pwd") String pwd);
}
