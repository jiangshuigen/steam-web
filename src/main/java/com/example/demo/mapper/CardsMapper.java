package com.example.demo.mapper;

import com.example.demo.dto.CardsQuery;
import com.example.demo.entity.Cards;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CardsMapper {

    List<Cards> getCardsList(CardsQuery query);

    int saveBatchCard(@Param("cardList") List<Cards> cardList);
}
