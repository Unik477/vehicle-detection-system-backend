package com.bbau.vehicledetection.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.bbau.vehicledetection.backend.entity.BlockedVehicle.BlockedVehicle;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendBlockedVehicleNotification(BlockedVehicle blockedVehicle) {
        System.out.println("Sending blocked vehicle notification: " + blockedVehicle.getVehicleNumber());
        messagingTemplate.convertAndSend("/topic/blocked-vehicle", blockedVehicle);
    }
}
