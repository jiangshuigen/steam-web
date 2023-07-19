package com.example.demo.web.admin;

import com.example.demo.config.ResultData;
import com.example.demo.entity.PromotionLevels;
import com.example.demo.service.PromotionLevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/admin/level")
@Api(value = "推广等级管理", tags = {"推广等级管理"})
public class PromotionLevelController {

    @Resource
    private PromotionLevelService promotionlevelservice;

    /**
     * 列表展示
     *
     * @param
     * @return
     */
    @ApiOperation(value = "获取推广level列表")
    @GetMapping("/getList")
    public ResultData<List<PromotionLevels>> getLevelList() {
        return ResultData.success(promotionlevelservice.getLevelList());
    }


    /**
     * 主键查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "主键查询")
    @GetMapping("/getLevel")
    public ResultData<PromotionLevels> getLevelById(@RequestParam int id) {
        return ResultData.success(promotionlevelservice.getLevelById(id));
    }


    /**
     * 修改level
     *
     * @param level
     * @return
     */
    @ApiOperation(value = "修改")
    @PostMapping("/updateLevel")
    public ResultData updateLevel(@RequestBody PromotionLevels level) {
        return ResultData.success(promotionlevelservice.updateLevel(level));
    }

    /**
     * 删除
     *
     * @param
     * @return
     */
    @ApiOperation(value = "删除")
    @DeleteMapping("/deleteLevel")
    public ResultData deleteLevel(@RequestParam int id) {
        return ResultData.success(promotionlevelservice.deleteLevel(id));
    }
}
