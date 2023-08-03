package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.dto.GameRankingDto;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface GameBattleService {
    int createEvent(GameArenasSaveDto info);

    PageInfo<GameArenasDto> getEventList(BattleWebQuery base);

    GameArenasDto getGameArenasDetail(int id);

    List<BattleDto> joinEvent(int id, UserDto user) throws Exception;

    PageInfo<GameArenasDto> getBattleList(BasePage query);

    PageInfo<GameArenasDto>  getMyBattleList(BattleQuery query);

    GameRankingDto getGameRankingList(GameRankingQuery query);

    int socket(int userId);
}
