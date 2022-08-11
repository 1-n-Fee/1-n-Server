package konkuk.nServer.domain.websocket.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class ResponseMessage {

    private String content;
    private String type; // 메시지 타입
    private Long postId; // 방번호
    private String sendTime;

    @Builder
    public ResponseMessage(String content, String type, Long postId, String sendTime) {
        this.content = content;
        this.type = type;
        this.postId = postId;
        this.sendTime = sendTime;
    }
}