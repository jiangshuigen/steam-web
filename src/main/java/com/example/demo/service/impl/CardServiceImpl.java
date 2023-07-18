package com.example.demo.service.impl;

import com.example.demo.dto.CardsDto;
import com.example.demo.dto.CardsQuery;
import com.example.demo.entity.Cards;
import com.example.demo.mapper.CardsMapper;
import com.example.demo.service.CardService;
import com.example.demo.util.CodeUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    @Resource
    private CardsMapper mapper;

    @Override
    public PageInfo<Cards> getCardsList(CardsQuery query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<Cards> list = mapper.getCardsList(query);
        PageInfo<Cards> listInfo = new PageInfo<>(list);
        return listInfo;
    }

    @Override
    public List<String> getCodeList(int numb) {
        List<String> listCode = new ArrayList<>();
        if (numb > 0) {
            for (int i = 0; i < numb; i++) {
                listCode.add(CodeUtils.getCode());
            }
        }
        return listCode;
    }

    @Override
    public int saveBatchCard(CardsDto entity) {
        List<Cards> cardList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(entity.getCodeList())) {
            entity.getCodeList().forEach(e -> {
                cardList.add(Cards.builder()
                        .number(e)
                        .bean(entity.getBean())
                        .build());
            });
        }
        return mapper.saveBatchCard(cardList);
    }
}
