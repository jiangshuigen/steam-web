package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.UUAward;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UUPService {
    //获取模板初始数据
    UUResponse getTemplateList();

    String importData(MultipartFile file);

    List<UUAwardDto> getUUList(UUQuery query);

    List<UUAwardDto> getWebUUList(BasePage query);

    List<UUAward> getUUAwardList(String templateHashName);

    UUResponse buyAwards(UUOrder dto);

    void callback(CallbackInfo info);

    List<UUawardsDto> getAllUUAwardList();

    void updateAwardsBean(UUawardsDto uUawardsDto);
}
