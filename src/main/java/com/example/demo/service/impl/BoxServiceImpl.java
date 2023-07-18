package com.example.demo.service.impl;

import com.example.demo.dto.BoxQuery;
import com.example.demo.entity.Box;
import com.example.demo.mapper.BoxMapper;
import com.example.demo.service.BoxService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BoxServiceImpl implements BoxService {

    @Resource
    private BoxMapper boxmapper;

    @Override
    public PageInfo<Box> getBoxList(BoxQuery query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<Box> list = boxmapper.getBoxList(query);
        PageInfo<Box> listInfo = new PageInfo<>(list);
        return listInfo;
    }

    @Override
    public Box getBoxById(int id) {
        return boxmapper.getBoxById(id);
    }

    @Override
    public int updateBox(Box box) {
        return boxmapper.updateBox(box);
    }

    @Override
    public int saveBox(Box box) {
        return boxmapper.saveBox(box);
    }
}
