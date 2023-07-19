package com.example.demo.web.admin;

import com.example.demo.config.ResultData;
import com.example.demo.entity.FirstRechargeOffer;
import com.example.demo.service.FirstRechargeOfferService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/admin/first")
@Api(value = "首冲奖励管理", tags = {"首冲奖励管理"})
public class FirstRechargeOfferController {
    @Resource
    private FirstRechargeOfferService firstrechargeofferservice;

    /**
     * 获取列表
     * @return
     */
    @ApiOperation(value = "获取列表")
    @GetMapping("/getFirstRechargeList")
    public ResultData<List<FirstRechargeOffer>> getFirstRechargeList() {
        return ResultData.success(firstrechargeofferservice.getFirstRechargeList());
    }

    /**
     * 主键查询
     * @param id
     * @return
     */
    @ApiOperation(value = "主键查询")
    @GetMapping("/getFirstRechargeById")
    public ResultData<FirstRechargeOffer> getFirstRechargeById(@RequestParam int id) {
        return ResultData.success(firstrechargeofferservice.getFirstRechargeById(id));
    }

    /**
     * 修改
     * @param firstrecharge
     * @return
     */
    @ApiOperation(value = "修改")
    @PostMapping("/update")
    public ResultData updateFirstRecharge(@RequestBody FirstRechargeOffer firstrecharge) {
        return ResultData.success(firstrechargeofferservice.updateFirstRecharge(firstrecharge));
    }

    /**
     * 新增
     * @param firstrecharge
     * @return
     */
    @ApiOperation(value = "新增")
    @PostMapping("/save")
    public ResultData saveFirstRecharge(@RequestBody FirstRechargeOffer firstrecharge) {
        return ResultData.success(firstrechargeofferservice.saveFirstRecharge(firstrecharge));
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @ApiOperation(value = "删除")
    @DeleteMapping("/delete")
    public ResultData deleteFirstRecharge(@RequestParam int id) {
        return ResultData.success(firstrechargeofferservice.deleteFirstRecharge(id));
    }
}
