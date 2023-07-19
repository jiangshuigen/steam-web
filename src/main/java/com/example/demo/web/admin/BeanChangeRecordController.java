package com.example.demo.web.admin;

import com.example.demo.config.ResultData;
import com.example.demo.dto.BasePage;
import com.example.demo.entity.BeanChangeRecords;
import com.example.demo.service.BeanChangeRecordService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/beanChangeRecord")
@Api(value = "奖励记录", tags = {"奖励记录"})
public class BeanChangeRecordController {

    @Resource
    private BeanChangeRecordService beanchangerecordservice;

    /**
     * 列表查询
     * @param query
     * @return
     */
    @ApiOperation(value = "获取列表")
    @PostMapping("/getList")
    public ResultData<PageInfo<BeanChangeRecords>> getBeanChangeRecordList(@RequestBody BasePage query) {
        return ResultData.success(beanchangerecordservice.getBeanChangeRecordList(query));
    }
}
