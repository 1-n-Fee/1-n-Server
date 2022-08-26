package konkuk.nServer.domain.websocket.controller;

import konkuk.nServer.domain.websocket.dto.response.FindRoom;
import konkuk.nServer.domain.websocket.service.ChatService;
import konkuk.nServer.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatService chatService;

    // 모든 채팅방 목록 반환
    @GetMapping("/room/{postId}")
    @ResponseBody
    public FindRoom findRoomInfo(@AuthenticationPrincipal PrincipalDetails userDetail,
                                 @PathVariable Long postId) {
        return chatService.findUserRoom(userDetail.getId(), postId);
    }

}

