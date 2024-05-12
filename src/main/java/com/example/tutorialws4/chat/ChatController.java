package com.example.tutorialws4.chat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ChatController {
    private static final Logger log = LogManager.getLogger(ChatController.class);

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;


    public ChatController(SimpMessagingTemplate messagingTemplate, ChatMessageService chatMessageService) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/chat")
    // вместо @SendTo указываем пункт назначения непосредственно в методе по отправке сообщения конкретному юзеру ({recipientId}/queue/messages)
    public void processMessage(@Payload ChatMessage chatMessage) {
        ChatMessage savedMessage = chatMessageService.save(chatMessage);

        ChatNotification chatNotification = new ChatNotification();
        chatNotification.setId(savedMessage.getId());
        chatNotification.setSenderId(savedMessage.getSenderId());
        chatNotification.setRecipientId(savedMessage.getRecipientId());
        chatNotification.setContent(savedMessage.getContent());

        messagingTemplate.convertAndSendToUser(chatMessage.getRecipientId(), "/queue/messages", chatNotification);
        log.info("Message sent to recipientId: {}", chatMessage.getRecipientId());
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable("senderId") String senderId, @PathVariable("recipientId") String recipientId) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId));
    }
}
