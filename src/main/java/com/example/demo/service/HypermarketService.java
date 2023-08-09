package com.example.demo.service;

import com.example.demo.dto.WebBoxAwardsQuery;
import com.example.demo.entity.BoxAwards;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;

public interface HypermarketService {
    PageInfo<BoxAwards> getAwardList(WebBoxAwardsQuery query);

    int buyAwards(HttpServletRequest request, int[] ids)throws Exception;
}
