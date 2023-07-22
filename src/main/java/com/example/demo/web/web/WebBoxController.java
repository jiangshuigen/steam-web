package com.example.demo.web.web;


import com.example.demo.config.ResultData;
import com.example.demo.dto.OpenBox;
import com.example.demo.entity.Box;
import com.example.demo.entity.BoxAwards;
import com.example.demo.entity.User;
import com.example.demo.service.BoxService;
import com.example.demo.service.LuckyBoxService;
import com.example.demo.service.UserService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/web/box")
@Api(value = "开箱(前台)", tags = {"开箱(前台)"})
public class WebBoxController {

    @Resource
    private UserService userservice;

    @Resource
    private BoxService boxservice;

    @Resource
    private LuckyBoxService luckyboxservice;

    /**
     * 展示宝箱列表
     *
     * @param
     * @return
     */
    @ApiOperation(value = "首页box列表")
    @PostMapping("/getIndexBoxList")
    public ResultData<PageInfo<Box>> getIndexBoxList() {
        return ResultData.success(boxservice.getIndexBoxList());
    }

    /**
     * 查询武器列表
     *
     * @param boxId
     * @return
     */
    @ApiOperation(value = "查看箱子武器列表")
    @PostMapping("/getBoxAwardList")
    public ResultData<List<BoxAwards>> getBoxAwardList(@RequestParam("boxId") int boxId) {
        return ResultData.success(luckyboxservice.getIndexBoxList(boxId));
    }

    /**
     * 开箱
     *
     * @param
     * @return
     */
    @ApiOperation(value = "开箱")
    @PostMapping("/openBox")
    public ResultData<Object> openBox(HttpServletRequest request, @RequestBody OpenBox openbox) throws Exception {
        //验证登录
        User usr = userservice.getLoginUserInfo(request);
        if (ObjectUtils.isEmpty(usr)) {
            return ResultData.fail("403", "请登录");
        }
        if (openbox.getNumb() > 5 || openbox.getNumb() < 1) {
            return ResultData.fail("403", "开箱数在1-5之间");
        }
        return ResultData.success(luckyboxservice.openBox(openbox, usr));
    }


}
