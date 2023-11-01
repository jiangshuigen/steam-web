package com.example.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.demo.dto.BoxQuery;
import com.example.demo.dto.SaveAwardsDto;
import com.example.demo.entity.Box;
import com.example.demo.entity.BoxAwards;
import com.example.demo.mapper.BoxMapper;
import com.example.demo.mapper.LuckyBoxMapper;
import com.example.demo.service.BoxService;
import com.example.demo.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BoxServiceImpl implements BoxService {

    @Resource
    private BoxMapper boxmapper;
    @Resource
    private LuckyBoxMapper mapper;
    @Resource
    private UserService userservice;
    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private LuckyBoxMapper luckyboxmapper;

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
        int i = boxmapper.updateBox(box);
        if (i > 0) {
            userservice.resetCache();
        }
        return i;
    }

    @Override
    public int saveBox(Box box) {
        int i = boxmapper.saveBox(box);
        if (i > 0) {
            userservice.resetCache();
        }
        return i;
    }

    @Override
    public List<Box> getIndexBoxList() {
        return boxmapper.getIndexBoxList();
    }

    @Override
    public List<Box> getGameArenaBoxList() {
        return boxmapper.getGameArenaBoxList();
    }

    @Override
    public int saveAward(SaveAwardsDto dto) {
        int i = mapper.saveAward(dto);
        if (i > 0) {
            //添加成功更新redis
            List<BoxAwards> list = luckyboxmapper.getIndexBoxList(dto.getBoxId());
            String reds = JSON.toJSONString(list);
            redisTemplate.opsForValue().set("BoxNumb-" + dto.getBoxId() + "|" + 0, reds);
            redisTemplate.opsForValue().set("BoxNumb-" + dto.getBoxId() + "|" + 1, reds);
        }
        return i;
    }

    @Override
    public int deleteBoxById(int id) {
        return boxmapper.deleteBoxById(id);
    }

    @Override
    public int deleteAward(int id) {
        return boxmapper.deleteAward(id);
    }

    @Override
    public BoxAwards getAwardsById(int id) {
        return mapper.getBoxAwardById(id);
    }
}
