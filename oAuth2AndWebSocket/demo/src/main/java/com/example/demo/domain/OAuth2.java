package com.example.demo.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class OAuth2 {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "oAuth2_id")
    private Long id;

    private String kakao;
    private String naver;
    private String google;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private User user;

    public OAuth2(User user) {
        this.user = user;
    }


    public void setKakao(String kakao) {
        this.kakao = kakao;
    }

    public void setNaver(String naver) {
        this.naver = naver;
    }

    public void setGoogle(String google) {
        this.google = google;
    }
}
