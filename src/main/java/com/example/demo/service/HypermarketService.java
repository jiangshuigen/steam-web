package com.example.demo.service;

import com.example.demo.dto.UUawardsDto;
import com.example.demo.dto.UUawardsQuery;
import com.example.demo.entity.BoxAwards;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;

public interface HypermarketService {
    PageInfo<UUawardsDto> getAwardList(UUawardsQuery query);

    int buyAwards(HttpServletRequest request, int[] ids) throws Exception;
}
