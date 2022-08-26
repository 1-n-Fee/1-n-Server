package konkuk.nServer.domain.account.dto.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailCertification {

    private String userEmail;
    private String code;

}
