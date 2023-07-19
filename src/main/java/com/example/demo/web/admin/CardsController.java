package com.example.demo.web.admin;

import com.example.demo.config.ResultData;
import com.example.demo.dto.CardsDto;
import com.example.demo.dto.CardsQuery;
import com.example.demo.entity.Cards;
import com.example.demo.service.CardService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/admin/card")
@Api(value = "卡密管理", tags = {"卡密管理"})
public class CardsController {
    @Resource
    private CardService cardservice;


    /**
     * 查询列表
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "查询列表")
    @PostMapping("/getCardsList")
    public ResultData<PageInfo<Cards>> getCardsList(@RequestBody CardsQuery query) {
        return ResultData.success(cardservice.getCardsList(query));
    }


    /**
     * 预生成code
     *
     * @param numb
     * @return
     */
    @ApiOperation(value = "预生成卡密")
    @GetMapping("/getCodeList")
    public ResultData<List<String>> getCodeList(@RequestParam int numb) {
        return ResultData.success(cardservice.getCodeList(numb));
    }

    @ApiOperation(value = "生成卡密")
    @PostMapping("/saveBatchCard")
    public ResultData saveBatchCard(@RequestBody CardsDto entity) {
        return ResultData.success(cardservice.saveBatchCard(entity));
    }
}
