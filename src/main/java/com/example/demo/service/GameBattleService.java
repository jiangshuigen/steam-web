package com.example.demo.service;

import com.example.demo.dto.BasePage;
import com.example.demo.dto.GameArenasDto;
import com.example.demo.dto.GameArenasSaveDto;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.BoxRecords;
import com.example.demo.entity.GameArenas;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface GameBattleService {
    int createEvent(GameArenasSaveDto info);

    PageInfo<GameArenasDto> getEventList(BasePage base);

    GameArenasDto getGameArenasDetail(int id);

    List<BoxRecords> joinEvent(int id, UserDto user) throws Exception;

}
