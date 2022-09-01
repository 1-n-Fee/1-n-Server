package konkuk.nServer.security.jwt;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import konkuk.nServer.exception.ExceptionEnum;
import konkuk.nServer.security.PrincipalDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtTokenProvider tokenProvider;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        super(authenticationManager);
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("요청 들어옴");

        String jwtHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        JwtClaim jwtClaim = null;

        // JWT 토큰을 검증을 해서 정상적인 사용자인지 확인
        if (jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
            log.info("헤더에 포함된 Authorization Bearer 토큰이 없습니다.");
            request.setAttribute("exception", ExceptionEnum.NOT_FOUND_TOKEN.getCode());
        }
        else {
            String jwtToken = jwtHeader.replace("Bearer ", "");
            try {
                jwtClaim = tokenProvider.validate(jwtToken);
            } catch (TokenExpiredException e) {
                e.printStackTrace();
                request.setAttribute("exception", ExceptionEnum.EXPIRED_TOKEN.getCode());
            } catch (JWTDecodeException e) {
                e.printStackTrace();
                request.setAttribute("exception", ExceptionEnum.INVALID_TOKEN.getCode());
            } catch (Exception e) {
                log.error("================================================");
                log.error("JwtFilter - doFilterInternal() 오류발생");
                log.error("Exception Message : {}", e.getMessage());
                log.error("Exception StackTrace : {");
                e.printStackTrace();
                log.error("}");
                log.error("================================================");
                request.setAttribute("exception", ExceptionEnum.INVALID_TOKEN.getCode());
            }

            log.info("JWT 검증 실패. jwtToken={}", jwtToken);
        }

        // 서명이 정상적으로 됨
        if (jwtClaim != null && jwtClaim.getId() != null) {

            PrincipalDetails principalDetails = new PrincipalDetails(jwtClaim.getId(), jwtClaim.getRole());
            log.info("JWT 검증 성공. id={}, Role={}", jwtClaim.getId(), jwtClaim.getRole());

            // Jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어준다.
            Authentication authentication
                    = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            // 시큐리티의 세션에 접근하여 Authentication 객체를 저장
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }
}
