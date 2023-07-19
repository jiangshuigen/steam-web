package com.example.demo.web.admin;

import com.example.demo.config.ResultData;
import com.example.demo.dto.ApplDto;
import com.example.demo.dto.ApplUpdate;
import com.example.demo.dto.ApplyQuery;
import com.example.demo.dto.DeliveryRecordQuery;
import com.example.demo.entity.BoxRecords;
import com.example.demo.entity.DeliveryRecord;
import com.example.demo.service.BoxRecordService;
import com.example.demo.service.DeliveryRecordService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/admin/delivery")
@Api(value = "发货管理", tags = {"发货管理"})
public class DeliveryRecordController {

    @Resource
    private DeliveryRecordService deliveryrecordservice;

    @Resource
    private BoxRecordService boxrecordservice;

    /**
     * 发货记录
     *
     * @return
     */
    @ApiOperation(value = "发货记录")
    @PostMapping("/getList")
    public ResultData<List<DeliveryRecord>> getDeliveryRecordList(@RequestBody DeliveryRecordQuery query) {
        return ResultData.success(deliveryrecordservice.getDeliveryRecordList(query));
    }


    /**
     * 申请列表
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "申请列表")
    @PostMapping("/getApplyList")
    public ResultData<PageInfo<ApplDto>> getApplyList(@RequestBody ApplyQuery query) {
        return ResultData.success(boxrecordservice.getApplyList(query));
    }

    @ApiOperation(value = "修改：退回/提货完成")
    @PostMapping("/updateApply")
    public ResultData<PageInfo<BoxRecords>> updateApply(@RequestBody ApplUpdate dto) {
        return ResultData.success(boxrecordservice.updateApply(dto));
    }
}
