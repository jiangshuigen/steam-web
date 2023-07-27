package com.example.demo.web.message;

import com.example.demo.config.Constant;
import com.example.demo.message.NoticeDto;
import com.example.demo.service.UserService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;

@Component
@RabbitListener(queues = Constant.USER_MESSAGE)
public class DirectReceiver {
    //
    @Resource
    private UserService userservice;

    @RabbitHandler(isDefault = true)
    public void process(@RequestBody NoticeDto ms) {
        System.out.println("DirectReceiver消费者收到消息  : " + ms.toString());
        userservice.insertUserMessage(ms);
    }
}