package com.carson.chatbot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.carson.chatbot.domain.ChatBotMessage;

@Component
public class ChatBotListener {
    @Autowired 
    private SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event){
        System.out.println("Recieved new connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username != null){

            ChatBotMessage chatMessage = new ChatBotMessage();
            chatMessage.setType("Leave");
            chatMessage.setSender("username");

            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
}
