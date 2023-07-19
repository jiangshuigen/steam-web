package com.example.demo.web.admin;

import com.example.demo.config.ResultData;
import com.example.demo.dto.BasePage;
import com.example.demo.entity.Vip;
import com.example.demo.service.VipService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/vip")
@Api(value = "vip管理", tags = {"vip管理"})
public class VipController {

    @Resource
    private VipService vipservice;

    /**
     * 查询列表
     *
     * @param
     * @return
     */
    @ApiOperation(value = "获取VIP列表")
    @PostMapping("/vipList")
    public ResultData<PageInfo<Vip>> getVipListByPage(@RequestBody BasePage page) {
        return ResultData.success(vipservice.getVipListByPage(page));
    }

    /**
     * 主键查询
     * @param id
     * @return
     */
    @ApiOperation(value = "主键查询")
    @GetMapping("/getVip")
    public ResultData<Vip> getVipById(@RequestParam int id) {
        return ResultData.success(vipservice.getVipById(id));
    }

    /**
     * 修改vip
     * @param vip
     * @return
     */
    @ApiOperation(value = "修改")
    @PostMapping("/updateVip")
    public ResultData updateVip(@RequestBody Vip vip) {
        return ResultData.success(vipservice.updateVip(vip));
    }
}
