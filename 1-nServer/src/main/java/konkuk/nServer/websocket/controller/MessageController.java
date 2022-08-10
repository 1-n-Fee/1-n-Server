package konkuk.nServer.websocket.controller;


import konkuk.nServer.websocket.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessageSendingOperations sendingOperations;

    @MessageMapping("/chat/message") // 클라이언트에서 /pub/chat/message로 메시지 발행
    public void enter(ChatMessage message) {
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 입장하였습니다.");
        }
        // /sub/chat/room/{roomId} 에 구독 중인 클라이언트에게 메시지를 보낸다.
        sendingOperations.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}
