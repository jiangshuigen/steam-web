package com.example.demo.web.web;

import com.example.demo.config.Constant;
import com.example.demo.config.ResultData;
import com.example.demo.dto.OpenDto;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.WebBoxAwardsQuery;
import com.example.demo.entity.AwardTypes;
import com.example.demo.entity.BoxAwards;
import com.example.demo.entity.LuckyBoxRecord;
import com.example.demo.service.LuckyBoxService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/web/luckyBox")
@Api(value = "幸运饰品", tags = {"幸运饰品"})
public class LuckyBoxWebController {
    @Resource
    private LuckyBoxService luckyboxservice;


    /**
     * 装备类型列表
     *
     * @return
     */
    @ApiOperation(value = "获取装备类型列表")
    @GetMapping("/getTypeList")
    public ResultData<List<AwardTypes>> getTypeList() {
        return ResultData.success(luckyboxservice.getTypeList());
    }

    /**
     * 按照装备类型查询装备列表
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "获取装备列表")
    @PostMapping("/getAwardList")
    public ResultData<List<BoxAwards>> getAwardList(@RequestBody WebBoxAwardsQuery query) {
        return ResultData.success(luckyboxservice.getWebAwardList(query));
    }

    /**
     * 查询所有人roll的进度
     *
     * @param
     * @return
     */
    @ApiOperation(value = "获取历史记录")
    @GetMapping("/getHistory")
    public ResultData<List<LuckyBoxRecord>> getHistory(@RequestParam int awardId) {
        return ResultData.success(luckyboxservice.getHistory(awardId));
    }

    /**
     * 开启幸运首饰
     *
     * @param open
     * @return
     */
    @ApiOperation(value = "开启")
    @PostMapping("/open")
    public ResultData<List<BoxAwards>> openAward(HttpServletRequest request, @RequestBody OpenDto open) {
        try {
            //获取session
            HttpSession session = request.getSession();
            UserDto dto = (UserDto) session.getAttribute(Constant.USER_INFO);
            if (ObjectUtils.isEmpty(dto)) {
                return ResultData.fail("403", "未登录");
            }
            return ResultData.success(luckyboxservice.openAward(open,dto));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.fail("500", e.getMessage());
        }

    }

}
