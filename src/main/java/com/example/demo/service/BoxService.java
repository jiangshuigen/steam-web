package com.example.demo.service;

import com.example.demo.dto.BoxQuery;
import com.example.demo.dto.GameArenasDto;
import com.example.demo.dto.SaveAwardsDto;
import com.example.demo.entity.Box;
import com.example.demo.entity.BoxAwards;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface BoxService {

    PageInfo<Box> getBoxList(BoxQuery query);

    Box getBoxById(int id);

    int updateBox(Box box);

    int saveBox(Box box);

    List<Box> getIndexBoxList();

    List<Box>  getGameArenaBoxList();

    int saveAward(SaveAwardsDto dto);

    int deleteBoxById(int id);

    int deleteAward(int id);

    BoxAwards getAwardsById(int id);
}
