package com.example.demo.web.admin;

import com.example.demo.config.ResultData;
import com.example.demo.dto.BoxRecordsQuery;
import com.example.demo.entity.BoxRecords;
import com.example.demo.service.BoxRecordService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/boxRecord")
@Api(value = "开箱记录", tags = {"开箱记录"})
public class BoxRecordsController {


    @Resource
    private BoxRecordService boxrecordservice;

    /**
     * 获取开箱记录列表
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "获取开箱记录列表")
    @PostMapping("/getList")
    public ResultData<PageInfo<BoxRecords>> getBoxList(@RequestBody BoxRecordsQuery query) {
        return ResultData.success(boxrecordservice.getBoxRecordList(query));
    }

    /**
     *  主键查询
     * @param id
     * @return
     */
    @ApiOperation(value = "主键查询")
    @GetMapping("/getRecordById")
    public ResultData<BoxRecords> getRecordById(@RequestParam int id) {
        return ResultData.success(boxrecordservice.getRecordById(id));
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @ApiOperation(value = "删除")
    @DeleteMapping("/deleteById")
    public ResultData deleteById(@RequestParam int id) {
        return ResultData.success(boxrecordservice.deleteById(id));
    }
}
