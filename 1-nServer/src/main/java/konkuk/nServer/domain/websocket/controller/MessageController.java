package konkuk.nServer.domain.websocket.controller;


import konkuk.nServer.domain.websocket.dto.request.RequestMessage;
import konkuk.nServer.domain.websocket.dto.response.ResponseMessage;
import konkuk.nServer.domain.websocket.service.ChatService;
import konkuk.nServer.exception.ApiException;
import konkuk.nServer.exception.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessageSendingOperations sendingOperations;
    private final ChatService chatService;

    @MessageMapping("/chat/message") // 클라이언트에서 /pub/chat/message 로 메시지 발행
    public void enter(RequestMessage message, SimpMessageHeaderAccessor messageHeaderAccessor) {
        log.info("메시지 받음. content={}", message.getContent());

        Object seesion = messageHeaderAccessor.getSessionAttributes().getOrDefault("userId", null);
        if (seesion == null) throw new ApiException(ExceptionEnum.NOT_LOGIN_CHAT);

        Long userId = (Long) seesion;

        ResponseMessage responseMessage = chatService.sendMessage(userId, message);

        // /sub/chat/room/{roomId} 에 구독 중인 클라이언트에게 메시지를 보낸다.
        sendingOperations.convertAndSend("/sub/chat/room/" + message.getPostId(), responseMessage);
    }

    @GetMapping("/message/{postId}")
    public List<ResponseMessage> findMessage(@PathVariable Long postId) {
        log.info("findMessage 실행");
        return chatService.findByPostId(postId);
    }


}
