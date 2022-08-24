package konkuk.nServer.domain.websocket.dto.response;

import konkuk.nServer.domain.websocket.domain.Message;
import lombok.Builder;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
public class ResponseMessage {

    private String content;
    private String type; // 메시지 타입
    private Long postId; // 방번호
    private String sendTime;
    private String sender;

    @Builder
    public ResponseMessage(String content, String type, Long postId, String sendTime, String sender) {
        this.content = content;
        this.type = type;
        this.postId = postId;
        this.sendTime = sendTime;
        this.sender = sender;
    }

    public static ResponseMessage of(Message message, Long postId) {
        return ResponseMessage.builder()
                .content(message.getContent())
                .type(message.getType().name())
                .postId(postId)
                .sendTime(message.getTime().format(DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm")))
                .sender(message.getUser().getNickname())
                .build();
    }
}
