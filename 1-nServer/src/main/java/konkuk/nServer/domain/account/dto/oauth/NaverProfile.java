package konkuk.nServer.domain.account.dto.oauth;

import lombok.Data;

@Data
public class NaverProfile {
    public String resultcode;
    public String message;
    public NaverResponse response;

    @Data
    public class NaverResponse {
        String id;
        String name;
    }
}
