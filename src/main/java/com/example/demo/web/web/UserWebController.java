package com.example.demo.web.web;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.example.demo.config.Constant;
import com.example.demo.config.ResultData;
import com.example.demo.dto.*;
import com.example.demo.entity.BoxRecordsWeb;
import com.example.demo.entity.User;
import com.example.demo.service.BoxRecordService;
import com.example.demo.service.UserService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
@RequestMapping("/web")
@Api(value = "用户中心(前台)", tags = {"用户中心(前台)"})
public class UserWebController {
    @Resource
    private UserService userservice;
    @Resource
    private BoxRecordService boxrecordservice;


    /**
     * 用户登录
     *
     * @param info
     * @return
     */
    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public ResultData login(HttpServletRequest request, @RequestBody LoginInfo info) {
        UserDto dto = userservice.userLogin(request, info);
        if (!ObjectUtils.isEmpty(dto)) {
            return ResultData.success(dto);
        } else {
            return ResultData.fail("403", "用户名密码错误");
        }

    }

    /**
     * 登出
     *
     * @param request
     * @param userName
     * @return
     */
    @ApiOperation(value = "用户登出")
    @GetMapping("/loginOut")
    public ResultData loginOut(HttpServletRequest request, @RequestParam("userNameOrPhone") String userName) {
        request.getSession().removeAttribute(Constant.USER_INFO);
        return ResultData.success(0);
    }

    /**
     * 用户中心
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "主键查询User")
    @GetMapping("/getUserById")
    public ResultData<UserDto> getUserInfo(@RequestParam("id") int id) {
        return ResultData.success(userservice.getUserInfo(id));
    }


    /**
     * 修改
     *
     * @param user
     * @return
     */
    @ApiOperation(value = "用户信息修改")
    @PostMapping("/updateUser")
    public ResultData updateUser(@RequestBody UserUpdateDto user) {
        return ResultData.success(userservice.webUpdateUser(user));
    }


    /**
     * 我的背包
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "我的背包")
    @PostMapping("/getList")
    public ResultData<PageInfo<BoxRecordsWeb>> getMyPackage(HttpServletRequest request, @RequestBody BoxRecordsWebQuery query) {
        //获取用户信息
        User usr = userservice.getLoginUserInfo(request);
        if (!ObjectUtils.isEmpty(usr)) {
            query.setUserId(usr.getId());
            return ResultData.success(boxrecordservice.getMyPackage(query));
        } else {
            return ResultData.fail("403", "请登录");
        }
    }

    /**
     * 获取图形验证码
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @ApiOperation(value = "获取图形验证码")
    @GetMapping("/getCode")
    public void getCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        //定义图形验证码的长、宽、验证码字符数、干扰线宽度
        ShearCaptcha shearCaptcha = CaptchaUtil.createShearCaptcha(150, 40, 5, 4);
        //图形验证码写出，可以写出到文件，也可以写出到流
        shearCaptcha.write(response.getOutputStream());
        //获取验证码中的文字内容
        System.out.println("code = " + shearCaptcha.getCode());
        request.getSession().setAttribute(Constant.REGISTER_CODE, shearCaptcha.getCode());
    }


    /**
     * 查重接口
     *
     * @param str
     * @param type 1-手机 2-用户名 3-邀请码
     * @return
     */
    @ApiOperation(value = "查重接口")
    @GetMapping("/repeatCheck")
    public ResultData repeatCheck(@RequestParam String str, @RequestParam("type(1-手机查重 2-用户名查重)") int type) {
        return ResultData.success(userservice.repeatCheck(str, type));
    }


    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    public ResultData register(HttpServletRequest request, @RequestBody UserRegisterDto user) throws Exception {
        return ResultData.success(userservice.register(request, user));
    }

    /**
     * 发送短信验证码
     *
     * @param
     * @param
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "发送短信")
    @GetMapping("/sendCode")
    public ResultData sendCode(@RequestParam String phone) {
        return ResultData.success(userservice.sendCode(phone));
    }

    /**
     * 物品取回
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "背包物品取回")
    @GetMapping("/getPackage")
    public ResultData getPackage(@RequestParam(value = "ids") int[] ids) {
        return ResultData.success(boxrecordservice.getPackage(ids));
    }

    /**
     * 兑换
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "兑换")
    @GetMapping("/exchange")
    public ResultData exchange(HttpServletRequest request, @RequestParam(value = "ids") int[] ids) {
        //获取session
        HttpSession session = request.getSession();
        UserDto dto = (UserDto) session.getAttribute(Constant.USER_INFO);
        if (ObjectUtils.isEmpty(dto)) {
            return ResultData.fail("403", "未登录");
        }
        return ResultData.success(boxrecordservice.exchange(request, ids));
    }

    /**
     * 我的取回记录
     *
     * @return
     */
    @ApiOperation(value = "取回记录")
    @PostMapping("/getBackList")
    public ResultData getBackList(HttpServletRequest request, @RequestBody BackQuery query) {
        //获取session
        HttpSession session = request.getSession();
        UserDto dto = (UserDto) session.getAttribute(Constant.USER_INFO);
        if (ObjectUtils.isEmpty(dto)) {
            return ResultData.fail("403", "未登录");
        }
        return ResultData.success(boxrecordservice.getBackList(request, query));
    }
}
