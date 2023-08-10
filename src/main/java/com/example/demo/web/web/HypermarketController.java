package com.example.demo.web.web;

import com.example.demo.config.Constant;
import com.example.demo.config.ResultData;
import com.example.demo.dto.UUawardsDto;
import com.example.demo.dto.UUawardsQuery;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.WebBoxAwardsQuery;
import com.example.demo.entity.BoxAwards;
import com.example.demo.service.HypermarketService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/web/market")
@Api(value = "商城(前台)", tags = {"商城(前台)"})
public class HypermarketController {


    @Resource
    private HypermarketService hypermarketservice;

    @ApiOperation(value = "获取装备列表")
    @PostMapping("/getAwardList")
    public ResultData<PageInfo<UUawardsDto>> getAwardList(@RequestBody UUawardsQuery query) {
        return ResultData.success(hypermarketservice.getAwardList(query));
    }

    @ApiOperation(value = "购买")
    @GetMapping("/buy")
    public ResultData buyAwards(HttpServletRequest request, @RequestParam(value = "ids") int[] ids) {
        //获取session
        HttpSession session = request.getSession();
        UserDto dto = (UserDto) session.getAttribute(Constant.USER_INFO);
        if (ObjectUtils.isEmpty(dto)) {
            return ResultData.fail("403", "未登录");
        }
        try {
            return ResultData.success(hypermarketservice.buyAwards(request, ids));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.fail("500", e.getMessage());
        }
    }
}
