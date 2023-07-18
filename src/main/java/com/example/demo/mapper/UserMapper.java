package com.example.demo.mapper;

import com.example.demo.dto.LoginIpLog;
import com.example.demo.dto.LoginIpLogQuery;
import com.example.demo.dto.UserQuery;
import com.example.demo.entity.Dashboard;
import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    Integer insertUser(User user);

    Integer batchInsertUser(List<User> users);

    User deleteUser(Integer id);

    int updateUser(User user);

    User selectUser(Integer id);

    List<User> selectAllUser();

    @Select("select * from t_user where username like CONCAT('%' , #{0} , '%')")
    @ResultMap("userMap")
    List<User> selectUserByName(String name);

    Dashboard getDashboard();

    List<User> getUserListByPage(UserQuery query);

    User getUserById(int id);

    List<LoginIpLog> getIPList(LoginIpLogQuery query);


}
