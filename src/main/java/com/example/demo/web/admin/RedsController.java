package com.example.demo.web.admin;

import com.example.demo.config.ResultData;
import com.example.demo.dto.RedQuery;
import com.example.demo.entity.Reds;
import com.example.demo.enumpakage.ResponseCode;
import com.example.demo.service.RedsService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/reds")
@Api(value = "红包管理", tags = {"红包管理"})
public class RedsController {

    @Resource
    private RedsService redsservice;

    /**
     * 红包列表
     *
     * @return
     */
    @ApiOperation(value = "获取红包列表")
    @PostMapping("/redsList")
    public ResultData<PageInfo<Reds>> getRedsListByPage(@RequestBody RedQuery query) {
        return ResultData.success(redsservice.getRedsListByPage(query));
    }

    /**
     * 主键查询
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "id获取红包")
    @GetMapping("/getReds")
    public ResultData getRedsById(@RequestParam int id) {
        return ResultData.success(redsservice.getRedsById(id));
    }

    /**
     * 删除红包
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除红包")
    @DeleteMapping("/deleteReds")
    public ResultData deleteRedsById(@RequestParam int id) {
        int i = redsservice.deleteRedsById(id);
        if (i > 0) {
            return ResultData.success(ResponseCode.SUCCESS);
        }
        return null;
    }

    /**
     * 新增红包
     *
     * @param
     * @return
     */
    @ApiOperation(value = "新增红包")
    @PostMapping("/saveReds")
    public ResultData saveReds(@RequestBody Reds reds) {
        int i = redsservice.saveReds(reds);
        if (i > 0) {
            return ResultData.success(ResponseCode.SUCCESS);
        }
        return null;
    }
}
