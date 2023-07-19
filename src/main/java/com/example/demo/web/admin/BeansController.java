package com.example.demo.web.admin;

import com.example.demo.config.ResultData;
import com.example.demo.dto.BeansQuery;
import com.example.demo.entity.Beans;
import com.example.demo.service.BeansService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/beans")
@Api(value = "金币管理", tags = {"金币管理"})
public class BeansController {

    @Resource
    private BeansService beansservice;

    /**
     * 列表查询
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "获取列表")
    @PostMapping("/getList")
    public ResultData<PageInfo<Beans>> getBoxList(@RequestBody BeansQuery query) {
        return ResultData.success(beansservice.getBeansList(query));
    }


    /**
     * 主键查询
     * @param id
     * @return
     */
    @ApiOperation(value = "主键查询")
    @GetMapping("/getBeanById")
    public ResultData<Beans> getBeanById(@RequestParam int id) {
        return ResultData.success(beansservice.getBeanById(id));
    }


    /**
     * 新建保存
     *
     * @param bean
     * @return
     */
    @ApiOperation(value = "保存")
    @PostMapping("/saveBeans")
    public ResultData saveBeans(@RequestBody Beans bean) {
        return ResultData.success(beansservice.saveBeans(bean));
    }

    /**
     * 修改
     * @param bean
     * @return
     */
    @ApiOperation(value = "修改")
    @PostMapping("/updateBeans")
    public ResultData updateBeans(@RequestBody Beans bean) {
        if (bean.getId() <= 0) {
            ResultData.fail("500", "参数错误：ID不存在");
        }
        return ResultData.success(beansservice.updateBeans(bean));
    }

    /**
     *  删除
     * @param id
     * @return
     */
    @ApiOperation(value = "删除")
    @DeleteMapping("/delete")
    public ResultData deleteBeanById(@RequestParam int id) {
        return ResultData.success(beansservice.deleteBeanById(id));
    }

}
