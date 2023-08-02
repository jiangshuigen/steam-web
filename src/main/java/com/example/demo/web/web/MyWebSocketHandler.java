package com.example.demo.web.web;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class MyWebSocketHandler extends TextWebSocketHandler {

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 处理收到的消息
        String receivedMessage = message.getPayload();
        // 发送消息给客户端
        session.sendMessage(new TextMessage("Received: " + receivedMessage));
    }
}