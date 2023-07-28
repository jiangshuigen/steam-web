package com.example.demo.web.admin;


import com.example.demo.config.ResultData;
import com.example.demo.entity.User;
import com.example.demo.entity.UserMessage;
import com.example.demo.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/message")
@Api(value = "发送站内信", tags = {"发送站内信"})
public class MessageController {

    @Resource
    private MessageService messageservice;

    /**
     *  发送消息
     * @param message
     * @return
     */
    @ApiOperation(value = "发消息")
    @PostMapping("/send")
    public ResultData sendMessage(@RequestBody UserMessage message) {
        return ResultData.success(messageservice.sendMessage(message));
    }


}
