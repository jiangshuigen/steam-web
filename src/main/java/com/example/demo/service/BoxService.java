package com.example.demo.service;

import com.example.demo.dto.BoxQuery;
import com.example.demo.entity.Box;
import com.github.pagehelper.PageInfo;

public interface BoxService {

    PageInfo<Box> getBoxList(BoxQuery query);

    Box getBoxById(int id);

    int updateBox(Box box);

    int saveBox(Box box);
}
