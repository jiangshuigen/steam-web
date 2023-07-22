package com.example.demo.mapper;

import com.example.demo.dto.BoxQuery;
import com.example.demo.entity.Box;

import java.util.List;

public interface BoxMapper {
    List<Box> getBoxList(BoxQuery query);

    Box getBoxById(int id);

    int updateBox(Box box);

    int saveBox(Box box);

    List<Box> getIndexBoxList();
}
