package com.example.demo.web.web;


import com.example.demo.config.ResultData;
import com.example.demo.entity.PromotionLevels;
import com.example.demo.service.PromotionLevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/web/promotion")
@Api(value = "推广等级管理", tags = {"推广等级管理"})
public class PromotionWebController {
    @Resource
    private PromotionLevelService promotionlevelservice;


    /**
     * 推广等级列表
     * @return
     */
    @ApiOperation(value = "获取推广level列表")
    @GetMapping("/getList")
    public ResultData<List<PromotionLevels>> getLevelList() {
        return ResultData.success(promotionlevelservice.getLevelList());
    }

}
