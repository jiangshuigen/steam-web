package com.example.demo.mapper;

import com.example.demo.dto.*;
import com.example.demo.entity.AwardTypes;
import com.example.demo.entity.BoxAwards;

import java.util.List;

public interface LuckyBoxMapper {

    List<AwardTypes> getTypeList();

    AwardTypes getTypeById(int id);

    int updateType(AwardTypes type);

    int saveType(AwardTypes type);

    List<BoxAwards> getAwardList(BoxAwardsQuery query);

    int updateAward(BoxAwards award);

    int deleteById(int id);

    List<BoxAwards> getIndexBoxList(int boxId);

    int updateAwardCount(AwardCountDto awardcountdto);

    List<BoxAwards> getBoxAwardList();

    List<BoxAwards> getWebAwardList(WebBoxAwardsQuery query);

    BoxAwards getBoxAwardById(int awardId);

    List<UUawardsDto> getSellAwardList(UUawardsQuery query);

    UUawardsDto getSellAwardById(int id);

    int saveAward(SaveAwardsDto dto);
}
