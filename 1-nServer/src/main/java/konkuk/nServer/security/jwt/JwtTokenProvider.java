package konkuk.nServer.security.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import konkuk.nServer.domain.user.domain.Role;
import konkuk.nServer.domain.user.domain.Storemanager;
import konkuk.nServer.domain.user.domain.User;
import konkuk.nServer.exception.ApiException;
import konkuk.nServer.exception.ExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Service
public class JwtTokenProvider {
    @Value("${token.secret}")
    private String SECRET_KEY;

    @Value("${token.expiration_time}")
    private String EXPIRATION_TIME;


    public String createJwt(User user) {
        return JWT.create()
                .withSubject("토큰이름입니다.")
                .withExpiresAt(new Date(System.currentTimeMillis() + Long.parseLong(EXPIRATION_TIME)))
                .withClaim("id", user.getId())
                .withClaim("role", "student") // TODO 추후 변경
                // .withClaim("username", user.getName())
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    public String createJwt(Storemanager storemanager) {
        return JWT.create()
                .withSubject("토큰이름입니다.")
                .withExpiresAt(new Date(System.currentTimeMillis() + Long.parseLong(EXPIRATION_TIME)))
                .withClaim("id", storemanager.getId())
                .withClaim("role", "storemanager")
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    public String createSocketJwt(Long userId) {
        return JWT.create()
                .withSubject("socketToken")
                .withExpiresAt(new Date(System.currentTimeMillis() + Duration.ofSeconds(30).toMillis()))
                .withClaim("id", userId)
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    public Long validateSocketToken(String token) {
        Long id = JWT.require(Algorithm.HMAC512(SECRET_KEY)).build()
                .verify(token)
                .getClaim("id")
                .asLong();
        return id;
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

        Role role = convertRole(roleString);

        return new JwtClaim(id, role);
    }

    private Role convertRole(String role) {
        if (Objects.equals(role, "student")) return Role.ROLE_STUDENT;
        else if (Objects.equals(role, "storemanager")) return Role.ROLE_STOREMANAGER;
        else throw new ApiException(ExceptionEnum.INCORRECT_ROLE);
    }
}
