package com.example.demo.web.admin;

import com.example.demo.config.ResultData;
import com.example.demo.dto.BeanRecordQuery;
import com.example.demo.entity.BeanRecord;
import com.example.demo.service.BeanRecordService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/beanRecord")
@Api(value = "充值记录", tags = {"充值记录"})
public class BeanRecordController {

    @Resource
    private BeanRecordService beanrecordservice;

    /**
     * 查询列表
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "获取列表")
    @PostMapping("/getList")
    public ResultData<PageInfo<BeanRecord>> getBeanRecordList(@RequestBody BeanRecordQuery query) {
        return ResultData.success(beanrecordservice.getBeanRecordList(query));
    }
}
