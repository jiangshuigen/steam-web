package com.example.demo.service.impl;

import com.example.demo.dto.BeansQuery;
import com.example.demo.entity.Beans;
import com.example.demo.mapper.BeansMapper;
import com.example.demo.service.BeansService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BeansServiceImpl implements BeansService {

    @Resource
    private BeansMapper beansmapper;

    @Override
    public PageInfo<Beans> getBeansList(BeansQuery query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<Beans> list = beansmapper.getBeansList(query);
        PageInfo<Beans> listInfo = new PageInfo<>(list);
        return listInfo;
    }

    @Override
    public int saveBeans(Beans bean) {
        return beansmapper.saveBeans(bean);
    }

    @Override
    public Beans getBeanById(int id) {
        return beansmapper.getBeanById(id);
    }

    @Override
    public int updateBeans(Beans bean) {
        /**
         * todo 查询重复
         */
        return beansmapper.updateBeans(bean);
    }

    @Override
    public int deleteBeanById(int id) {
        return beansmapper.deleteBeanById(id);
    }
}
