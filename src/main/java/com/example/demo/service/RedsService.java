package com.example.demo.service;

import com.example.demo.dto.RedQuery;
import com.example.demo.entity.Reds;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface RedsService {
    /**
     * 红包列表
     * @param query
     * @return
     */
    PageInfo<Reds> getRedsListByPage(RedQuery query);

    Reds getRedsById(int id);

    int deleteRedsById(int id);

    int saveReds(Reds reds);
}
