package com.example.demo.web.admin;

import com.example.demo.config.ResultData;
import com.example.demo.dto.*;
import com.example.demo.entity.Box;
import com.example.demo.service.BoxService;
import com.example.demo.service.HypermarketService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/box")
@Api(value = "宝箱管理", tags = {"宝箱管理"})
public class BoxsController {


    @Resource
    private BoxService boxservice;
    @Resource
    private HypermarketService hypermarketservice;

    /**
     * 获取宝箱列表
     *
     * @return
     */
    @ApiOperation(value = "获取推广box列表")
    @PostMapping("/getBoxList")
    public ResultData<PageInfo<Box>> getBoxList(@RequestBody BoxQuery query) {
        return ResultData.success(boxservice.getBoxList(query));
    }

    /**
     * 主键查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除BOX")
    @GetMapping("/deleteBox")
    public ResultData<Box> deleteBoxById(@RequestParam int id) {
        return ResultData.success(boxservice.deleteBoxById(id));
    }

    @ApiOperation(value = "主键查询")
    @GetMapping("/getBox")
    public ResultData<Box> getBoxById(@RequestParam int id) {
        return ResultData.success(boxservice.getBoxById(id));
    }


    /**
     * 修改宝箱
     *
     * @param box
     * @return
     */
    @ApiOperation(value = "修改box")
    @PostMapping("/updateBox")
    public ResultData updateBox(@RequestBody Box box) {
        if (box.getId() <= 0) {
            ResultData.fail("500", "参数错误：ID不存在");
        }
        return ResultData.success(boxservice.updateBox(box));
    }

    /**
     * @param box
     * @return
     */
    @ApiOperation(value = "新增")
    @PostMapping("/saveBox")
    public ResultData saveBox(@RequestBody Box box) {
        return ResultData.success(boxservice.saveBox(box));
    }

    /**
     * 获取可选择的装备列表
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "获取可选择的装备列表")
    @PostMapping("/getAwardList")
    public ResultData<PageInfo<UUawardsDto>> getAwardList(@RequestBody AwardQuery query) {
        UUawardsQuery queryVo = new UUawardsQuery();
        BeanUtils.copyProperties(query, queryVo);
        queryVo.setIsBattle(1);//对战
        queryVo.setName(query.getName());
        return ResultData.success(hypermarketservice.getAwardList(queryVo));
    }

    /**
     * 新增奖品
     *
     * @param dto
     * @return
     */
    @ApiOperation(value = "新增奖品")
    @PostMapping("/saveAward")
    public ResultData updateAward(@RequestBody SaveAwardsDto dto) {
        return ResultData.success(boxservice.saveAward(dto));
    }

}
