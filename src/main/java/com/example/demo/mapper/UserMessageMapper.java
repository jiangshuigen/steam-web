package com.example.demo.mapper;

import com.example.demo.dto.MessageQuery;
import com.example.demo.entity.UserMessage;
import com.example.demo.message.NoticeDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMessageMapper {
    int insertUserMessage(NoticeDto ms);

    List<UserMessage> getMessageList(MessageQuery query);

    int batchList(@Param("ids") int[] ids);

}
