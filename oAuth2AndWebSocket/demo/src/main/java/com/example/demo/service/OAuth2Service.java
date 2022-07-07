package com.example.demo.service;

import com.example.demo.domain.OAuth2;
import com.example.demo.domain.User;
import com.example.demo.repository.OAuth2Repository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.dto.KakaoProfile;
import com.example.demo.service.dto.OAuthToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2Service {

    private final OAuth2Repository oAuth2Repository;
    private final UserRepository userRepository;

    public User findMemberByKakao(String kakaoId) {
        Optional<OAuth2> findOAuth = oAuth2Repository.findByKakao(kakaoId);
        return findOAuth.map(OAuth2::getUser).orElse(null);
    }

    public OAuthToken getKakaoToken(String code) {
        RestTemplate rt = new RestTemplate();

        // HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "437bc2fb95b24ca5a80d5763e4619f54");
        params.add("redirect_uri", "http://localhost:8080/auth/kakao/callback");
        params.add("code", code);

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        // Http 요청하기 - 응답받기
        ResponseEntity<String> response = rt.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST, kakaoTokenRequest, String.class);

        // Gson, Json Simple, ObjectMapper(내장)음 등 json을 매핑하는 라이브러리 있음

        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oAuthToken = null;
        try {
            oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonMappingException e) {
            log.info("JsonMappingException", e);
        } catch (JsonProcessingException e) {
            log.info("JsonProcessingException", e);
        }

        log.info("카카오 엑세스 토큰={}", oAuthToken.getAccess_token());
        return oAuthToken;
    }

    public KakaoProfile getKakaoProfile(String accessToken) {
        RestTemplate rt = new RestTemplate();

        // HttpHeader 오브젝트 생성
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer " + accessToken);
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers2);

        // Http 요청하기 - 응답받기
        ResponseEntity<String> response2 =
                rt.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST, kakaoProfileRequest, String.class);


        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
        } catch (JsonMappingException e) {
            log.info("JsonMappingException", e);
        } catch (JsonProcessingException e) {
            log.info("JsonProcessingException", e);
        }
        log.info("id={}, email={}", kakaoProfile.getId(), kakaoProfile.getKakao_account().getEmail());

        return kakaoProfile;
    }

    @Transactional
    public void interlockUserToKakao(User user, String kakaoId) {
        Optional<OAuth2> oAuth2 = oAuth2Repository.findByUserId(user.getId());
        if(oAuth2.isPresent()){
            oAuth2.get().setKakao(kakaoId);
        }
        else log.info("해당 인증 user가 존재하지 않습니다.");
    }
}
