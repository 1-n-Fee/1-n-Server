package konkuk.nServer.security.jwt;

import konkuk.nServer.domain.user.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtClaim {

    private Long id;
    private Role role;
}
