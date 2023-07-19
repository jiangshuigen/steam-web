package com.example.demo.web.admin;

import com.example.demo.config.ResultData;
import com.example.demo.dto.RedKeyQuery;
import com.example.demo.dto.RedKeyDto;
import com.example.demo.entity.RedKey;
import com.example.demo.enumpakage.ResponseCode;
import com.example.demo.service.RedKeyService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/redKey")
@Api(value = "口令红包管理", tags = {"口令红包管理"})
public class RedKeyController {

    @Resource
    private RedKeyService redkeyservice;

    /**
     * 获取口令红包列表
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "获取红包列表")
    @PostMapping("/redKeyList")
    public ResultData<PageInfo<RedKey>> getRedsListByPage(@RequestBody RedKeyQuery query) {
        return ResultData.success(redkeyservice.getRedsListByPage(query));
    }

    @ApiOperation(value = "生成口令红包")
    @PostMapping("/saveRedKey")
    public ResultData saveRedKey(@RequestBody RedKeyDto redkeydto) {
        int i = redkeyservice.saveRedKey(redkeydto);
        if (i > 0) {
            return ResultData.success(ResponseCode.SUCCESS);
        }
        return null;
    }
}
