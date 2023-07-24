package com.example.demo.mapper;

import com.example.demo.dto.BasePage;
import com.example.demo.dto.BattleQuery;
import com.example.demo.dto.GameArenasDto;
import com.example.demo.dto.GameArenasSaveDto;
import com.example.demo.entity.GameArenaBox;
import com.example.demo.entity.GameArenaUsers;
import com.example.demo.entity.GameAwardRecords;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface GameBattleMapper {

    int createEvent(GameArenasSaveDto info);

    List<GameArenasDto> getEventList(BasePage base);

    int insertArenaBox(GameArenaBox bx);

    int insertArenaUsers(GameArenaUsers us);

    GameArenasDto getGameArenasDetail(int id);

    int update(GameArenasDto dto);

    int saveGameAwardRecords(@Param("records") List<GameAwardRecords> gameawardrecords);

    List<GameArenasDto> getBattleList(BasePage query);

    List<GameArenasDto> getMyBattleList(BattleQuery query);

    BigDecimal getTotalBean(int id);
}
