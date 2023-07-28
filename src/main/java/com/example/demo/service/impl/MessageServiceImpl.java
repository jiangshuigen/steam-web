package com.example.demo.service.impl;

import com.example.demo.entity.UserMessage;
import com.example.demo.mapper.UserMessageMapper;
import com.example.demo.message.NoticeDto;
import com.example.demo.service.MessageService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private UserMessageMapper usermessagemapper;

    @Override
    public int sendMessage(UserMessage message) {
        NoticeDto dto = new NoticeDto();
        BeanUtils.copyProperties(message,dto);
        return usermessagemapper.insertUserMessage(dto);
    }
}
