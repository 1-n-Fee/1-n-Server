package konkuk.nServer.domain.user.dto.requestForm;

import lombok.Data;

@Data
public class Oauth2Login {

    private String kakaoCode;
    private String naverCode;
    private String googleCode;
}
