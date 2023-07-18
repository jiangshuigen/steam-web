package com.example.demo.mapper;

import com.example.demo.dto.BeansQuery;
import com.example.demo.entity.Beans;

import java.util.List;

public interface BeansMapper {
    List<Beans> getBeansList(BeansQuery query);

    int saveBeans(Beans bean);

    Beans getBeanById(int id);

    int updateBeans(Beans bean);

    int deleteBeanById(int id);
}
