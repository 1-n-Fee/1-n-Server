package konkuk.nServer.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import konkuk.nServer.domain.user.dto.oauth.*;
import konkuk.nServer.domain.user.repository.GoogleRepository;
import konkuk.nServer.domain.user.repository.KakaoRepository;
import konkuk.nServer.domain.user.repository.NaverRepository;
import konkuk.nServer.domain.user.repository.UserRepository;
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

    private final KakaoRepository kakaoRepository;
    private final NaverRepository naverRepository;
    private final GoogleRepository googleRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public String getKakaoId(String code) {
        // HttpHeader ???????????? ??????
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody ???????????? ??????
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("code", code);

        // HttpHeader??? HttpBody??? ????????? ??????????????? ??????
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        // Http ???????????? - ????????????
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST, kakaoTokenRequest, String.class);

        // Gson, Json Simple, ObjectMapper(??????)??? ??? json??? ???????????? ??????????????? ??????
        KakaoOAuthToken kakaoOAuthToken = mappingJsonToClass(response.getBody(), KakaoOAuthToken.class);

        KakaoProfile kakaoProfile = getKakaoProfile(kakaoOAuthToken.getAccess_token());
        return kakaoProfile.getId().toString();
    }

    private KakaoProfile getKakaoProfile(String accessToken) {
        String apiURL = "https://kapi.kakao.com/v2/user/me";
        String responseBody = getProfileByAccessToken(apiURL, accessToken);

        KakaoProfile kakaoProfile = mappingJsonToClass(responseBody, KakaoProfile.class);
        return kakaoProfile;
    }

    public String getNaverId(String authorizationCode) {
        RestTemplate rt = new RestTemplate();

        // HttpHeader ???????????? ??????
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody ???????????? ??????
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", naverClientId);
        params.add("client_secret", naverClientSecret);
        params.add("code", authorizationCode);
        params.add("state", naverState);

        // HttpHeader??? HttpBody??? ????????? ??????????????? ??????
        HttpEntity<MultiValueMap<String, String>> naverTokenRequest = new HttpEntity<>(params, headers);

        // Http ???????????? - ????????????
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

    public String getGoogleId(String authorizationCode) {
        // HttpHeader ???????????? ??????
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody ???????????? ??????
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret);
        params.add("redirect_uri", "http://localhost:3000/auth/google");
        params.add("code", authorizationCode);

        // HttpHeader??? HttpBody??? ????????? ??????????????? ??????
        HttpEntity<MultiValueMap<String, String>> googleTokenRequest = new HttpEntity<>(params, headers);

        // Http ???????????? - ????????????
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
            if (responseCode == HttpURLConnection.HTTP_OK) { // ?????? ??????
                return readBody(con.getInputStream());
            } else { // ?????? ??????
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API ????????? ?????? ??????", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL??? ?????????????????????. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("????????? ??????????????????. : " + apiUrl, e);
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
            throw new RuntimeException("API ????????? ????????? ??????????????????.", e);
        }
    }
}

