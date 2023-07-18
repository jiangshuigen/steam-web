package com.example.demo.web;

import com.example.demo.config.ResultData;
import com.example.demo.dto.RedKeyQuery;
import com.example.demo.dto.RedRecordQuery;
import com.example.demo.entity.RedRecords;
import com.example.demo.service.RedRecordsService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/redRecords")
@Api(value = "红包记录", tags = {"红包记录"})
public class RedRecordsController {

    @Resource
    private RedRecordsService redrecordsservice;

    /**
     * 获取红包记录列表
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "获取红包记录列表")
    @PostMapping("/getRecordsList")
    public ResultData<PageInfo<RedRecords>> getRecordsListByPage(@RequestBody RedRecordQuery query) {
        return ResultData.success(redrecordsservice.getRecordsListByPage(query));
    }

}
