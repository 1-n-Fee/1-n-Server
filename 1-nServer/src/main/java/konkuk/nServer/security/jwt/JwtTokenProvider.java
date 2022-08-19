package konkuk.nServer.security.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import konkuk.nServer.domain.common.service.ConvertProvider;
import konkuk.nServer.domain.storemanager.domain.Storemanager;
import konkuk.nServer.domain.user.domain.Role;
import konkuk.nServer.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${token.secret}")
    private String SECRET_KEY;

    @Value("${token.expiration_time}")
    private String EXPIRATION_TIME;

    private final ConvertProvider convertProvider;

    public String createJwt(Long id, Role role) {
        return JWT.create()
                .withSubject("토큰이름입니다.")
                .withExpiresAt(new Date(System.currentTimeMillis() + Long.parseLong(EXPIRATION_TIME)))
                .withClaim("id", id)
                .withClaim("role", role.name())
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    public String createJwt(User user) {
        return JWT.create()
                .withSubject("토큰이름입니다.")
                .withExpiresAt(new Date(System.currentTimeMillis() + Long.parseLong(EXPIRATION_TIME)))
                .withClaim("id", user.getId())
                .withClaim("role", user.getRole().name())
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    public String createJwt(Storemanager storemanager) {
        return JWT.create()
                .withSubject("토큰이름입니다.")
                .withExpiresAt(new Date(System.currentTimeMillis() + Long.parseLong(EXPIRATION_TIME)))
                .withClaim("id", storemanager.getId())
                .withClaim("role", storemanager.getRole().name())
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    public String createSocketJwt(Long userId) {
        return JWT.create()
                .withSubject("socketToken")
                .withExpiresAt(new Date(System.currentTimeMillis() + Duration.ofSeconds(30).toMillis()))
                .withClaim("id", userId)
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    public JwtClaim validate(String token) {
        Long id = JWT.require(Algorithm.HMAC512(SECRET_KEY)).build()
                .verify(token)
                .getClaim("id")
                .asLong();

        String roleString = JWT.require(Algorithm.HMAC512(SECRET_KEY)).build()
                .verify(token)
                .getClaim("role")
                .asString();

        Role role = convertProvider.convertRole(roleString);

        return new JwtClaim(id, role);
    }

    public Long validateSocketToken(String token) {
        return JWT.require(Algorithm.HMAC512(SECRET_KEY)).build()
                .verify(token)
                .getClaim("id")
                .asLong();
    }
}
