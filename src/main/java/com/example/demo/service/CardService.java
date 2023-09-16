package com.example.demo.service;

import com.example.demo.dto.CardsDto;
import com.example.demo.dto.CardsQuery;
import com.example.demo.entity.Cards;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CardService {
    PageInfo<Cards> getCardsList(CardsQuery query);

    List<String> getCodeList(int numb);

    int saveBatchCard(CardsDto entity);

    int updateCardByNumber(String number);
}
