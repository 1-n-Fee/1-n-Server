package konkuk.nServer.domain.account.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import konkuk.nServer.domain.account.domain.AccountType;
import konkuk.nServer.domain.account.dto.oauth.*;
import konkuk.nServer.exception.ApiException;
import konkuk.nServer.exception.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2Provider {

    @Value("${oauth2.kakao.client_id}")
    private String kakaoClientId;

    @Value("${oauth2.kakao.redirect_uri}")
    private String kakaoRedirectUri;

    @Value("${oauth2.naver.client_id}")
    private String naverClientId;

    @Value("${oauth2.naver.client_secret}")
    private String naverClientSecret;

    @Value("${oauth2.naver.state}")
    private String naverState;

    @Value("${oauth2.google.client_id}")
    private String googleClientId;

    @Value("${oauth2.google.client_secret}")
    private String googleClientSecret;
    private final ObjectMapper objectMapper;

    public String getOauthId(AccountType accountType, String code) {
        if (accountType == AccountType.KAKAO) return getKakaoId(code);
        if (accountType == AccountType.NAVER) return getNaverId(code);
        if (accountType == AccountType.GOOGLE) return getGoogleId(code);
        throw new ApiException(ExceptionEnum.INCORRECT_ACCOUNT_TYPE);
    }

    private String getKakaoId(String code) {
        // HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("code", code);

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        // Http 요청하기 - 응답받기
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST, kakaoTokenRequest, String.class);

        // Gson, Json Simple, ObjectMapper(내장)음 등 json을 매핑하는 라이브러리 있음
        KakaoOAuthToken kakaoOAuthToken = mappingJsonToClass(response.getBody(), KakaoOAuthToken.class);
        log.info("kakao AccessToken을 얻어왔습니다. token={}", kakaoOAuthToken.getAccess_token());

        KakaoProfile kakaoProfile = getKakaoProfile(kakaoOAuthToken.getAccess_token());
        log.info("카카오 profile 조회 완료");
        return kakaoProfile.getId().toString();
    }

    private KakaoProfile getKakaoProfile(String accessToken) {
        String apiURL = "https://kapi.kakao.com/v2/user/me";
        String responseBody = getProfileByAccessToken(apiURL, accessToken);

        KakaoProfile kakaoProfile = mappingJsonToClass(responseBody, KakaoProfile.class);
        return kakaoProfile;
    }

    private String getNaverId(String authorizationCode) {
        RestTemplate rt = new RestTemplate();

        // HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", naverClientId);
        params.add("client_secret", naverClientSecret);
        params.add("code", authorizationCode);
        params.add("state", naverState);

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> naverTokenRequest = new HttpEntity<>(params, headers);

        // Http 요청하기 - 응답받기
        ResponseEntity<String> response = rt.exchange("https://nid.naver.com/oauth2.0/token", HttpMethod.POST, naverTokenRequest, String.class);

        NaverOAuthToken naverOAuthToken = mappingJsonToClass(response.getBody(), NaverOAuthToken.class);
        NaverProfile naverProfile = getNaverProfile(naverOAuthToken.getAccess_token());
        return naverProfile.getResponse().getId().toString();
    }

    private NaverProfile getNaverProfile(String accessToken) {
        String apiURL = "https://openapi.naver.com/v1/nid/me";
        String responseBody = getProfileByAccessToken(apiURL, accessToken);

        NaverProfile naverProfile = mappingJsonToClass(responseBody, NaverProfile.class);
        return naverProfile;
    }

    private String getGoogleId(String authorizationCode) {
        // HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret);
        params.add("redirect_uri", "http://localhost:3000/auth/google");
        params.add("code", authorizationCode);

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> googleTokenRequest = new HttpEntity<>(params, headers);

        // Http 요청하기 - 응답받기
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange("https://oauth2.googleapis.com/token", HttpMethod.POST, googleTokenRequest, String.class);

        GoogleOAuthToken googleOAuthToken = mappingJsonToClass(response.getBody(), GoogleOAuthToken.class);
        GoogleProfile googleProfile = getGoogleProfile(googleOAuthToken.getAccess_token());
        return googleProfile.getId();
    }

    private GoogleProfile getGoogleProfile(String accessToken) {
        String apiURL = "https://www.googleapis.com/oauth2/v2/userinfo";
        String responseBody = getProfileByAccessToken(apiURL, accessToken);

        GoogleProfile googleProfile = mappingJsonToClass(responseBody, GoogleProfile.class);
        return googleProfile;
    }

    private <T> T mappingJsonToClass(String content, Class<T> classType) {
        try {
            return objectMapper.readValue(content, classType);
        } catch (JsonMappingException e) {
            log.info("JsonMappingException", e);
        } catch (JsonProcessingException e) {
            log.info("JsonProcessingException", e);
        }
        return null;
    }

    private static String getProfileByAccessToken(String apiUrl, String accessToken) {

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", "Bearer " + accessToken);

        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }
            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }
}

