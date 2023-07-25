package com.example.demo.mapper;

import com.example.demo.dto.*;
import com.example.demo.entity.GameArenaBox;
import com.example.demo.entity.GameArenaUsers;
import com.example.demo.entity.GameAwardRecords;
import com.example.demo.entity.GameRanking;
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

    GameRanking queryRankUserList(int userId);

    int updateRanking(GameRanking ranking);

    int saveRanking(GameRanking ranking);

    List<GameRanking> getGameRankingList(GameRankingQuery query);
}
