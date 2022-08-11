package konkuk.nServer.domain.websocket.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class FindRoom {

    private Long postId;
    private String storeName;
    private boolean isOwner;
    private String postState;

    @Builder
    public FindRoom(Long postId, String storeName, boolean isOwner, String postState) {
        this.postId = postId;
        this.storeName = storeName;
        this.isOwner = isOwner;
        this.postState = postState;
    }
}
