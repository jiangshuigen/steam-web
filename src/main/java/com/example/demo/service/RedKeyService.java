package com.example.demo.service;

import com.example.demo.dto.RedKeyDto;
import com.example.demo.dto.RedKeyQuery;
import com.example.demo.entity.RedKey;
import com.github.pagehelper.PageInfo;

public interface RedKeyService {
    PageInfo<RedKey> getRedsListByPage(RedKeyQuery query);

    int saveRedKey(RedKeyDto redkeydto);
}
