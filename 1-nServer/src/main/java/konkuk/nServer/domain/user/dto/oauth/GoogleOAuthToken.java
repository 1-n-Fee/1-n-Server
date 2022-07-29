package konkuk.nServer.domain.user.dto.oauth;

import lombok.Data;

@Data
public class GoogleOAuthToken {
    private String access_token;
    private int expires_in;
    private String scope;
    private String token_type;
    private String id_token;
}
