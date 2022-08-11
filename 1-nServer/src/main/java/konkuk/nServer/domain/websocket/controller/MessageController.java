package konkuk.nServer.domain.websocket.controller;


import konkuk.nServer.domain.websocket.domain.MessageType;
import konkuk.nServer.domain.websocket.dto.request.RequestMessage;
import konkuk.nServer.domain.websocket.dto.response.ResponseMessage;
import konkuk.nServer.domain.websocket.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.messaging.simp.stomp.StompHeaders.SESSION;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessageSendingOperations sendingOperations;
    private final ChatService chatService;

    @MessageMapping("/chat/message") // 클라이언트에서 /pub/chat/message로 메시지 발행
    public void enter(RequestMessage message, SimpMessageHeaderAccessor messageHeaderAccessor) {
        messageHeaderAccessor.getSessionAttributes().get(SESSION).ge

        if (MessageType.ENTER.name().equals(message.getType())) {
            message.setContent(message.getSender() + "님이 입장하였습니다.");
        }

        ResponseMessage responseMessage = chatService.sendMessage(message);

        // /sub/chat/room/{roomId} 에 구독 중인 클라이언트에게 메시지를 보낸다.
        sendingOperations.convertAndSend("/sub/chat/room/" + message.getPostId(), responseMessage);
    }
}
