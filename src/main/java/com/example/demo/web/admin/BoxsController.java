package com.example.demo.web.admin;

import com.example.demo.config.ResultData;
import com.example.demo.dto.BoxQuery;
import com.example.demo.entity.Box;
import com.example.demo.service.BoxService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/box")
@Api(value = "宝箱管理", tags = {"宝箱管理"})
public class BoxsController {


    @Resource
    private BoxService boxservice;

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
     *
     * @param box
     * @return
     */
    @ApiOperation(value = "新增")
    @PostMapping("/saveBox")
    public ResultData saveBox(@RequestBody Box box) {
        return ResultData.success(boxservice.saveBox(box));
    }
}
