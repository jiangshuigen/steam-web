package com.example.demo.service.impl;

import com.example.demo.dto.RedQuery;
import com.example.demo.entity.Reds;
import com.example.demo.mapper.RedsMapper;
import com.example.demo.service.RedsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RedsServiceImpl implements RedsService {

    @Resource
    private RedsMapper redsmapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public PageInfo<Reds> getRedsListByPage(RedQuery query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<Reds> list = redsmapper.getRedsListByPage(query);
        PageInfo<Reds> listInfo = new PageInfo<>(list);
        return listInfo;
    }

    @Override
    public Reds getRedsById(int id) {
        return redsmapper.getRedsById(id);
    }

    @Override
    public int deleteRedsById(int id) {
        return redsmapper.deleteRedsById(id);
    }

    @Override
    public int saveReds(Reds reds) {
        redsmapper.saveReds(reds);
        //维护库存
        try {
            redisTemplate.opsForValue().set("RedPackage|" + reds.getId(), reds.getNum());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    @Override
    public int updateNumbReds(Reds red) {
        redisTemplate.opsForValue().set("RedPackage|" + red.getId(), red.getNum());
        return redsmapper.updateNumbReds(red);
    }
}
