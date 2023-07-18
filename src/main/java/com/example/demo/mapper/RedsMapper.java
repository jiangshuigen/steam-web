package com.example.demo.mapper;

import com.example.demo.dto.RedQuery;
import com.example.demo.entity.Reds;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RedsMapper {

    List<Reds> getRedsListByPage(RedQuery query);

    Reds getRedsById(int id);

    int deleteRedsById(int id);

    int saveReds(Reds reds);
}
