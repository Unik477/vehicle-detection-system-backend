package com.bbau.vehicledetection.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketSender {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void send(String destination, Object payload) {
        messagingTemplate.convertAndSend(destination, payload);
    }
}
