package com.example.tutorialws4.chat;

import lombok.Data;

@Data
public class ChatNotification {
    private String id;
    private String senderId;
    private String recipientId;
    private String content;
}
