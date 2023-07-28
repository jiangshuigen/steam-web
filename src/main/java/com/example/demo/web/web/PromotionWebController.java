package com.example.demo.web.web;


import com.example.demo.config.ResultData;
import com.example.demo.dto.PromotionInfo;
import com.example.demo.dto.PromotionQuery;
import com.example.demo.dto.PromotionUser;
import com.example.demo.entity.PromotionLevels;
import com.example.demo.entity.User;
import com.example.demo.service.PromotionLevelService;
import com.example.demo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/web/promotion")
@Api(value = "推广中心", tags = {"推广中心"})
public class PromotionWebController {
    @Resource
    private PromotionLevelService promotionlevelservice;

    @Resource
    private UserService userservice;

    /**
     * 推广等级列表
     *
     * @return
     */
    @ApiOperation(value = "获取推广level列表")
    @GetMapping("/getList")
    public ResultData<List<PromotionLevels>> getLevelList() {
        return ResultData.success(promotionlevelservice.getLevelList());
    }


    /**
     * 推广中心
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "推广中心")
    @GetMapping("/getPromotionInfo")
    public ResultData<PromotionInfo> getPromotionInfo(HttpServletRequest request) {
        //获取用户信息
        User usr = userservice.getLoginUserInfo(request);
        if (!ObjectUtils.isEmpty(usr)) {
            return ResultData.success(promotionlevelservice.getPromotionInfo(usr.getId()));
        } else {
            return ResultData.fail("403", "请登录");
        }
    }

    /**
     * 我的邀请记录
     *
     * @return
     */
    @ApiOperation(value = "邀请记录")
    @PostMapping("/getPromotionList")
    public ResultData<List<PromotionUser>> getPromotionList(HttpServletRequest request, @RequestBody PromotionQuery query) {

        //获取用户信息
        User usr = userservice.getLoginUserInfo(request);
        if (!ObjectUtils.isEmpty(usr)) {
            query.setUserId(usr.getId());
            return ResultData.success(promotionlevelservice.getPromotionList(query));
        } else {
            return ResultData.fail("403", "请登录");
        }
    }

}
