package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.security.PrincipalDetails;
import com.example.demo.service.OAuth2Service;
import com.example.demo.service.UserService;
import com.example.demo.service.dto.KakaoProfile;
import com.example.demo.service.dto.OAuthCode;
import com.example.demo.service.dto.OAuthToken;
import com.example.demo.service.dto.SignupDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final OAuth2Service oAuth2Service;
    private final JwtTokenProvider tokenProvider;

    @GetMapping("/")
    public String home() {
        String ret = "로그인 API 테스트 홈 화면입니다.(데이터는 JSON으로 보내주세요.)\n";
        ret += "1. 로그인(POST, /login) : email, password\n";
        ret += "2. 회원가입(POST, /signup) : name, email, password, role(USER 또는 ADMIN)\n";
        ret += "3. User와 Admin이 접근 가능한 서비스(GET, /user)\n";
        ret += "4. Admin만 접근 가능한 서비스(GET, /admin)\n";
        ret += "5. oAuth 관련 서비스 (자료 참고)\n";
        return ret;
    }


    @PostMapping("/signup")
    public String signup(@RequestBody SignupDto dto) {
        User user = userService.signup(dto);
        log.info("회원가입 완료. name={}, email={}, password={}", user.getName(), user.getEmail(), user.getPassword());
        return "회원가입 완료. \nname=" + user.getName() + ", email=" + user.getEmail() +
                ", password(암호화됨)=" + user.getPassword() + ", role=" + user.getRole().toString();
    }

    @GetMapping("/admin")
    public String adminPage(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        User user = principal.getUser();

        String ret = "이 페이지는 admin만 접근 가능한 api입니다.\n";
        ret += "name=" + user.getName() + ", email=" + user.getEmail() +
                ", password(암호화됨)=" + user.getPassword() + ", role=" + user.getRole().toString();
        return ret;
    }


    @GetMapping("/user")
    public String userAndAdminPage(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        User user = principal.getUser();
        String ret = "이 페이지는 user와 admin이 접근 가능한 api입니다.\n";
        ret += "name=" + user.getName() + ", email=" + user.getEmail() +
                ", password(암호화됨)=" + user.getPassword() + ", role=" + user.getRole().toString();
        return ret;
    }

    @GetMapping("/auth/kakao/callback")
    public String kakaoCallback(String code, HttpServletResponse response, Authentication authentication) {
        //log.info("카카오 oAuth2 인증 성공. code={}", code);
        log.info("카카오 oAuth2 인증 성공");

        OAuthToken kakaoToken = oAuth2Service.getKakaoToken(code);
        KakaoProfile kakaoProfile = oAuth2Service.getKakaoProfile(kakaoToken.getAccess_token());

        if(authentication!=null){
            PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
            User user = principal.getUser();

            oAuth2Service.interlockUserToKakao(user, String.valueOf(kakaoProfile.getId()));

            String ret = "계정과 oAuth2 연동 성공\n";
            ret += "name=" + user.getName() + ", email=" + user.getEmail() +
                    ", password(암호화됨)=" + user.getPassword() + ", role=" + user.getRole().toString();
            return ret;
        }


        /*
        Post : https://kauth.kakao.com/oauth/token
        grant_type=authorization_code
        client_id=437bc2fb95b24ca5a80d5763e4619f54
        redirect_uri=http://localhost:8080/auth/kakao/callback
        code={응답받은 코드 넣기}
         */



        String kakaoId = kakaoProfile.getId().toString();

        User user = oAuth2Service.findMemberByKakao(kakaoId);
        if (user == null) { // 회원 가입 필요
            return "회원가입이 되어 있지 않은 회원입니다.\n";
        } else {
            String jwtToken = tokenProvider.createJwt(user);
            response.addHeader("Authorization", "Bearer " + jwtToken);
            log.info("Token = {}", jwtToken);
            String ret = "oAuth2 로그인 성공\n";
            ret += "name=" + user.getName() + ", email=" + user.getEmail() +
                    ", password(암호화됨)=" + user.getPassword() + ", role=" + user.getRole().toString();
            return ret;
        }
    }

}
