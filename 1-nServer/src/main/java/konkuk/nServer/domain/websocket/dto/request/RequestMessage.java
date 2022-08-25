package konkuk.nServer.domain.websocket.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestMessage {

    private String content;
    private String type; // 메시지 타입
    private Long postId; // 방번호
    private String nickname;


    @Builder
    public RequestMessage(String content, String type, Long postId, String nickname) {
        this.content = content;
        this.type = type;
        this.postId = postId;
        this.nickname = nickname;
    }
}
