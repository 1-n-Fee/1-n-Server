package konkuk.nServer.security.jwt;

import konkuk.nServer.domain.user.domain.Password;
import konkuk.nServer.domain.user.domain.User;
import konkuk.nServer.exception.ExceptionEnum;
import konkuk.nServer.domain.user.repository.UserRepository;
import konkuk.nServer.exception.ApiException;
import konkuk.nServer.security.PrincipalDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

// 시큐리티가 가진 filer 중 BasicAuthenticationFilter가 있음
// 권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 타게 되어있음
// 만약 권한이나 인증이 필요한 주소가 아니라면 이 필터를 안탐
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    @Value("@{token.secret}")
    private String SECRET;

    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository,
                                  JwtTokenProvider tokenProvider) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    // 인증이나 권한이 필요한 주소 요청이 있을 때 해당 필터를 타게 됨
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("인증이나 권한이 필요한 주소 요청이 됨.");

        String jwtHeader = request.getHeader("Authorization");

        // JWT 토큰을 검증을 해서 정상적인 사용자인지 확인
        if (jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
            log.info("헤더에 포함된 Authorization Bearer 토큰이 없습니다.");
            chain.doFilter(request, response);
            return;
        }

        String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = tokenProvider.validateAndGetUserId(jwtToken);
        log.info("userId={}", userId);


        // 서명이 정상적으로 됨
        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));
            log.info("인가 성공 userId={}", user.getId());

            PrincipalDetails principalDetails = new PrincipalDetails(user, new Password()); // 여기서 password는 의미없음

            // Jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어준다.
            Authentication authentication
                    = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            // SecurityContext에 authentication을 넣기 위해 생성 -> set -> 컨텍스트로 등록
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);

            // 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장
            //SecurityContextHolder.getContext().setAuthentication(authentication);
        } else log.info("인가 실패. jwtToken={}", jwtToken);
        chain.doFilter(request, response);
    }
}
