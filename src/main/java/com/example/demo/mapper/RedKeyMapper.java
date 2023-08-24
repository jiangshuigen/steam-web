package com.example.demo.mapper;

import com.example.demo.dto.RedKeyQuery;
import com.example.demo.entity.RedKey;

import java.util.List;

public interface RedKeyMapper {

    List<RedKey> getRedsListByPage(RedKeyQuery query);

    int saveRedKey(RedKey redkey);

    RedKey getRedsByKey(String keyCode);

    int updateStatus(String keyCode);
}
