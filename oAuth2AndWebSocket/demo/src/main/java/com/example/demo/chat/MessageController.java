package com.example.demo.chat;


import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessageSendingOperations sendingOperations;

    @MessageMapping("/chat/message") // 클라이언트에서 /app/chat/message로 메시지 발행
    public void enter(ChatMessage message) {
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender()+"님이 입장하였습니다.");
        }
        // /topic/chat/room/방번호 에 구독 중인 클라이언트에게 메시지를 보낸다.
        sendingOperations.convertAndSend("/topic/chat/room/"+message.getRoomId(),message);
    }
}
