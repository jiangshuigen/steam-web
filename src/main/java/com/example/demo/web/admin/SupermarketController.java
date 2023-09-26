package com.example.demo.web.admin;

import com.example.demo.config.ResultData;
import com.example.demo.dto.UUawardsDto;
import com.example.demo.dto.UUawardsQuery;
import com.example.demo.service.HypermarketService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/market")
@Api(value = "商城（后台）", tags = {"商城(后台)"})
public class SupermarketController {

    @Resource
    private HypermarketService hypermarketservice;

    @ApiOperation(value = "获取装备列表")
    @PostMapping("/getAwardList")
    public ResultData<PageInfo<UUawardsDto>> getAwardList(@RequestBody UUawardsQuery query) {
        return ResultData.success(hypermarketservice.getAwardList(query));
    }

    @ApiOperation(value = "重新刷新价格")
    @GetMapping("/reload")
    public ResultData<Integer> reloadAwardsPrice(@RequestParam Integer id) {
        return ResultData.success(hypermarketservice.reloadAwardsPrice(id));
    }
}
