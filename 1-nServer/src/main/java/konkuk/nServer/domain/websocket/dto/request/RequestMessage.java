package konkuk.nServer.domain.websocket.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestMessage {

    private String content;
    private String type; // 메시지 타입
    private Long postId; // 방번호
    private String nickname;

}
