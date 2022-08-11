package konkuk.nServer.domain.websocket.controller;


import konkuk.nServer.domain.websocket.dto.request.RequestMessage;
import konkuk.nServer.domain.websocket.dto.response.ResponseMessage;
import konkuk.nServer.domain.websocket.service.ChatService;
import konkuk.nServer.exception.ApiException;
import konkuk.nServer.exception.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessageSendingOperations sendingOperations;
    private final ChatService chatService;

    @MessageMapping("/chat/message") // 클라이언트에서 /pub/chat/message로 메시지 발행
    public void enter(RequestMessage message, SimpMessageHeaderAccessor messageHeaderAccessor) {
        Object seesion = messageHeaderAccessor.getSessionAttributes().getOrDefault("userId", null);
        if (seesion == null) throw new ApiException(ExceptionEnum.NOT_LOGIN_CHAT);

        Long userId = (Long) seesion;

        ResponseMessage responseMessage = chatService.sendMessage(userId, message);

        // /sub/chat/room/{roomId} 에 구독 중인 클라이언트에게 메시지를 보낸다.
        sendingOperations.convertAndSend("/sub/chat/room/" + message.getPostId(), responseMessage);
    }
}
