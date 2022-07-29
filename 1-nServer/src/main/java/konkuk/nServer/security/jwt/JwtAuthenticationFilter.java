package konkuk.nServer.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import konkuk.nServer.exception.ApiException;
import konkuk.nServer.security.PrincipalDetails;
import konkuk.nServer.security.exception.SecurityExceptionEnum;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    @Value("${token.expiration_time}")
    private String EXPIRATION_TIME;

    @Value("${token.secret}")
    private String SECRET_KEY;

    private static final String DEFAULT_LOGIN_REQUEST_URL = "/login";
    private static final String HTTP_METHOD = "POST";    //HTTP 메서드의 방식은 POST 이다.
    private static final String CONTENT_TYPE = "application/json";//json 타입의 데이터로만 로그인을 진행한다.


    // login 요청을 하면 로그인 시도를 위해 실행
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 1. username, password 받아서
        String email = null, password = null;
        try {
            String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            Map<String, String> emailPasswordMap = objectMapper.readValue(messageBody, Map.class);
            email = emailPasswordMap.get("email");
            password = emailPasswordMap.get("password");
        } catch (IOException e) {
            throw new ApiException(SecurityExceptionEnum.INCORRECT_LOGIN);
        }


        log.info("로그인 요청 email={}, password={}", email, password);

        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(email, password);

        // 정상인지 로그인 시도를 해보기
        // authenticationManager로 로그인 시도를 하면 PrincipalDetailsService.loadUserByUsername()이 실행됨
        // 정상실행 된다는 건, 로그인 성공이라는 뜻 (인증 성공)
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // authentication 에는 로그인 정보가 담김
        log.info("로그인 성공 name={}", authentication.getName());
        return authentication; // return 되면 세션에 저장됨 (권한 관리를 위해 세션에 저장. 필요 없다면 안해도 괜찮다)

    }

    // 로그인 성공이면 이 함수 실행
    // JWT 토큰을 만들어서 request 요청한 사용자에게 JWT 토큰을 response 해주면 됨
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        log.info("successfulAuthentication 실행");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String jwtToken = tokenProvider.createJwt(principalDetails.getUser());

        response.addHeader("Authorization", "Bearer " + jwtToken);
        log.info("Token = {}", jwtToken);
    }

    @Data
    private static class LoginRequest {
        String email;
        String password;
    }
}
