package com.example.demo.service.impl;

import com.example.demo.dto.RedKeyDto;
import com.example.demo.dto.RedKeyQuery;
import com.example.demo.entity.RedKey;
import com.example.demo.mapper.RedKeyMapper;
import com.example.demo.service.RedKeyService;
import com.example.demo.util.CodeUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RedKeyServiceImpl implements RedKeyService {

    @Resource
    private RedKeyMapper redkeymapper;

    @Override
    public PageInfo<RedKey> getRedsListByPage(RedKeyQuery query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<RedKey> list = redkeymapper.getRedsListByPage(query);
        PageInfo<RedKey> listInfo = new PageInfo<>(list);
        return listInfo;
    }

    @Override
    @Transactional
    public int saveRedKey(RedKeyDto redkeydto) {
        int flag = 0;
        if (redkeydto.getNumb() > 0) {
            for (int i = 0; i < redkeydto.getNumb(); i++) {
                RedKey redkey = new RedKey();
                //口令
                redkey.setCode(CodeUtils.getCode());
                redkey.setBean(redkeydto.getBean());
                flag = redkeymapper.saveRedKey(redkey);
            }
        }
        return flag;
    }
}
