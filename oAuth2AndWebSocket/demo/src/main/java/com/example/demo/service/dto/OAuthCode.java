package com.example.demo.service.dto;

import lombok.Data;

@Data
public class OAuthCode {
    private String kakao;
    private String google;
    private String naver;
}
