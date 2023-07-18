package com.example.demo.service;

import com.example.demo.dto.BeansQuery;
import com.example.demo.entity.Beans;
import com.github.pagehelper.PageInfo;

public interface BeansService {

    PageInfo<Beans> getBeansList(BeansQuery query);

    int saveBeans(Beans bean);

    Beans getBeanById(int id);

    int updateBeans(Beans bean);

    int deleteBeanById(int id);
}
