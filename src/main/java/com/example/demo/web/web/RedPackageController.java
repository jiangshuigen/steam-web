package com.example.demo.web.web;

import com.example.demo.config.Constant;
import com.example.demo.config.ResultData;
import com.example.demo.dto.RedQuery;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.BoxAwards;
import com.example.demo.entity.Reds;
import com.example.demo.service.RedPackageService;
import com.example.demo.service.RedsService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/web/redPackage")
@Api(value = "红包中心", tags = {"红包中心"})
public class RedPackageController {

    @Resource
    private RedPackageService redpackageservice;
    @Resource
    private RedsService redsservice;


    /**
     * 查询红包（抢）
     *
     * @param
     * @return
     */
    @ApiOperation(value = "抢红包-查询我可抢的红包列表")
    @PostMapping("/getPackageList")
    public ResultData<PageInfo<Reds>> getPackageList(@RequestBody RedQuery query) {
        query.setStatus(1);
        return ResultData.success(redsservice.getRedsListByPage(query));
    }


    /**
     * 抢红包
     *
     * @param request
     * @param redId
     * @return
     */
    @ApiOperation(value = "抢红包")
    @PostMapping("/snatch")
    public ResultData<String> snatchPackage(HttpServletRequest request, @RequestParam("id") int redId) {
        //获取session
        HttpSession session = request.getSession();
        UserDto dto = (UserDto) session.getAttribute(Constant.USER_INFO);
        if (ObjectUtils.isEmpty(dto)) {
            return ResultData.fail("403", "未登录");
        }
        try {
            return ResultData.success(redpackageservice.snatchPackage(dto, redId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.fail("500", e.getMessage());
        }
    }


    /**
     * 输入口令领取口令红包
     * @param request
     * @param keyCode
     * @return
     */
    @ApiOperation(value = "领取口令红包")
    @PostMapping("/keyPackage")
    public ResultData<String> snatchKeyPackage(HttpServletRequest request, @RequestParam("key") String keyCode) {
        //获取session
        HttpSession session = request.getSession();
        UserDto dto = (UserDto) session.getAttribute(Constant.USER_INFO);
        if (ObjectUtils.isEmpty(dto)) {
            return ResultData.fail("403", "未登录");
        }
        try {
            return ResultData.success(redpackageservice.snatchKeyPackage(dto, keyCode));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.fail("500", e.getMessage());
        }
    }
}
