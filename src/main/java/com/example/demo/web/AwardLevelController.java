package com.example.demo.web;

import com.example.demo.config.ResultData;
import com.example.demo.dto.BoxQuery;
import com.example.demo.entity.AwardLevels;
import com.example.demo.service.AwardLevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/admin/awardLevel")
@Api(value="武器等级管理",tags={"武器等级管理"})
public class AwardLevelController {

    @Resource
    private AwardLevelService awardlevelservice;

    /**
     * 获取列表
     * @return
     */
    @ApiOperation(value = "获取武器等级列表")
    @GetMapping("/getList")
    public ResultData<List<AwardLevels>> getBoxList() {
        return ResultData.success(awardlevelservice.getAwardLevelList());
    }

    /**
     * 主键查询
     * @param id
     * @return
     */
    @ApiOperation(value = "主键查询")
    @GetMapping("/getLevelById")
    public ResultData<AwardLevels> getLevelById(@RequestParam int id) {
        return ResultData.success(awardlevelservice.getLevelById(id));
    }

    /**
     * 修改
     * @param level
     * @return
     */
    @ApiOperation(value = "修改")
    @PostMapping("/updateLevel")
    public ResultData updateLevel(@RequestBody AwardLevels level) {
        if (level.getId() <= 0) {
            ResultData.fail("500", "参数错误：ID不存在");
        }
        return ResultData.success(awardlevelservice.updateLevel(level));
    }
}
