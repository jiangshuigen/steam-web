package com.example.demo.web;

import com.example.demo.config.ResultData;
import com.example.demo.dto.BoxAwardsQuery;
import com.example.demo.dto.LuckyBboxRecordQuery;
import com.example.demo.entity.AwardTypes;
import com.example.demo.entity.BoxAwards;
import com.example.demo.entity.LuckyBboxRecord;
import com.example.demo.service.LuckyBoxService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/admin/luckyBox")
@Api(value = "幸运开箱", tags = {"幸运开箱"})
public class LuckyBoxController {
    @Resource
    private LuckyBoxService luckyboxservice;

    /**
     * 查询装备类型列表
     *
     * @return
     */
    @ApiOperation(value = "获取装备类型列表")
    @GetMapping("/getTypeList")
    public ResultData<List<AwardTypes>> getTypeList() {
        return ResultData.success(luckyboxservice.getTypeList());
    }

    /**
     * 查询type
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "主键查询")
    @GetMapping("/getTypeById")
    public ResultData<AwardTypes> getTypeById(@RequestParam int id) {
        return ResultData.success(luckyboxservice.getTypeById(id));
    }

    @ApiOperation(value = "删除装备类型")
    @DeleteMapping("/deleteById")
    public ResultData<AwardTypes> deleteById(@RequestParam int id) {
        return ResultData.success(luckyboxservice.deleteById(id));
    }

    /**
     * 修改装备类型
     *
     * @param type
     * @return
     */
    @ApiOperation(value = "修改类型")
    @PostMapping("/updateType")
    public ResultData updateType(@RequestBody AwardTypes type) {
        if (type.getId() <= 0) {
            ResultData.fail("500", "参数错误：ID不存在");
        }
        return ResultData.success(luckyboxservice.updateType(type));
    }

    /**
     * 新增
     *
     * @param type
     * @return
     */
    @ApiOperation(value = "新增装备类型")
    @PostMapping("/saveType")
    public ResultData saveType(@RequestBody AwardTypes type) {
        return ResultData.success(luckyboxservice.saveType(type));
    }

    /*****************装备管理***************************************************/
    /**
     * 获取武器列表
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "获取装备列表")
    @PostMapping("/getAwardList")
    public ResultData<PageInfo<BoxAwards>> getAwardList(@RequestBody BoxAwardsQuery query) {
        return ResultData.success(luckyboxservice.getAwardList(query));
    }

    /**
     * 修改武器
     *
     * @param award
     * @return
     */
    @ApiOperation(value = "修改")
    @PostMapping("/updateAward")
    public ResultData updateAward(@RequestBody BoxAwards award) {
        return ResultData.success(luckyboxservice.updateAward(award));
    }

    /**
     * 获取幸运开箱列表
     * @param query
     * @return
     */
    @ApiOperation(value = "幸运开箱记录")
    @PostMapping("/getLuckyBoxList")
    public ResultData<PageInfo<LuckyBboxRecord>> getLuckyBoxList(@RequestBody LuckyBboxRecordQuery query) {
        return ResultData.success(luckyboxservice.getLuckyBoxList(query));
    }
}
