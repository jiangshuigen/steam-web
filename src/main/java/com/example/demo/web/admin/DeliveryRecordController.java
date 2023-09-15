package com.example.demo.web.admin;

import com.example.demo.config.ResultData;
import com.example.demo.dto.*;
import com.example.demo.entity.BoxRecords;
import com.example.demo.entity.DeliveryRecord;
import com.example.demo.entity.UUSaleRsponse;
import com.example.demo.service.BoxRecordService;
import com.example.demo.service.DeliveryRecordService;
import com.example.demo.service.UUPService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @Resource
    private UUPService uupservice;

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


    /**
     * 在售查询
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "在售查询")
    @PostMapping("/getSellList")
    public ResultData<UUSaleRsponse> getSellList(@RequestBody SellerRecordQuery query) {
        return ResultData.success(deliveryrecordservice.getSellList(query));
    }


    /**
     * 查询基础数据
     *
     * @return
     */
    @ApiOperation(value = "（优品）查询模板ID")
    @PostMapping("/getTemplateList")
    public ResultData<UUResponse> getTemplateList() {
        return ResultData.success(uupservice.getTemplateList());
    }


    @ApiOperation(value = "（优品）数据导入")
    @PostMapping("/importData")
    public ResultData<String> importData(MultipartFile file) {
        return ResultData.success(uupservice.importData(file));
    }


    @ApiOperation(value = "（优品）获取商品基础数据(入库)")
    @PostMapping("/getUUList")
    public ResultData<PageInfo<UUAwardDto>> getUUList(@RequestBody BasePage query) {
        return ResultData.success(uupservice.getWebUUList(query));
    }

    /**
     * 查询商品列表
     * @param templateHashName
     * @return
     */
    @ApiOperation(value = "（优品）查询商品列表-（在售查询）")
    @PostMapping("/getUUAwardList")
    public ResultData<UUResponse> getUUAwardList(@RequestParam String templateHashName) {
        return ResultData.success(uupservice.getUUAwardList(templateHashName));
    }

    /**
     * 下单购买
     * @param dto
     * @return
     */
    @ApiOperation(value = "（优品）指定商品购买（购买发货）")
    @PostMapping("/buyAwards")
    public ResultData<UUResponse> buyAwards(@RequestBody UUOrder dto) {
        return ResultData.success(uupservice.buyAwards(dto));
    }

}
