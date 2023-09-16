package com.example.demo.service.impl;

import com.example.demo.dto.CardsDto;
import com.example.demo.dto.CardsQuery;
import com.example.demo.entity.BeanRecord;
import com.example.demo.entity.Cards;
import com.example.demo.mapper.CardsMapper;
import com.example.demo.service.BeanRecordService;
import com.example.demo.service.CardService;
import com.example.demo.util.CodeUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    @Resource
    private CardsMapper mapper;
    @Resource
    private BeanRecordService beanrecordservice;

    @Override
    public PageInfo<Cards> getCardsList(CardsQuery query) {
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<Cards> list = mapper.getCardsList(query);
        PageInfo<Cards> listInfo = new PageInfo<>(list);
        return listInfo;
    }

    @Override
    public List<String> getCodeList(int numb) {
        List<String> listCode = new ArrayList<>();
        if (numb > 0) {
            for (int i = 0; i < numb; i++) {
                listCode.add(CodeUtils.getCode());
            }
        }
        return listCode;
    }

    @Override
    public int saveBatchCard(CardsDto entity) {
        List<Cards> cardList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(entity.getCodeList())) {
            entity.getCodeList().forEach(e -> {
                cardList.add(Cards.builder()
                        .number(e)
                        .bean(entity.getBean())
                        .build());
                //生成订单
                BeanRecord record = new BeanRecord();
                record.setUserId(0);
                record.setInviterId(0);
                record.setBean(entity.getBean());
                record.setPrice(entity.getBean());
                record.setFinallyPrice(entity.getBean());
                DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
                String localDate = LocalDateTime.now().format(ofPattern);
                String orderNum = "CDK" + localDate;
                record.setCode(orderNum);
                record.setTradeNo(e);
                record.setStatus(0);
                record.setIsBenefit(0);
                record.setIsPayApi(0);
                beanrecordservice.insertBeanReacord(record);
            });
        }
        return mapper.saveBatchCard(cardList);
    }

    @Override
    public int updateCardByNumber(String number) {
        return mapper.updateCardByNumber(number);
    }
}
