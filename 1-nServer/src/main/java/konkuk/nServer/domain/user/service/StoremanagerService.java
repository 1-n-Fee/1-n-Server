package konkuk.nServer.domain.user.service;

import konkuk.nServer.domain.user.domain.*;
import konkuk.nServer.domain.user.dto.requestForm.StoremanagerSignup;
import konkuk.nServer.domain.user.repository.*;
import konkuk.nServer.exception.ApiException;
import konkuk.nServer.exception.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class StoremanagerService {

    private final StoremanagerRepository storemanagerRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final KakaoRepository kakaoRepository;
    private final NaverRepository naverRepository;
    private final GoogleRepository googleRepository;
    private final PasswordRepository passwordRepository;
    private final OAuth2Provider oAuth2Provider;

    public void signup(StoremanagerSignup form) {

        Role role = convertStoremanagerRole(form.getRole());
        AccountType accountType = convertAccountType(form.getAccountType());

        if (role != Role.ROLE_STOREMANAGER) throw new ApiException(ExceptionEnum.INCORRECT_ROLE);

        Storemanager storemanager = Storemanager.builder()
                .accountType(accountType)
                .storeRegistrationNumber(form.getStoreRegistrationNumber())
                .name(form.getName())
                .phone(form.getPhone())
                .email(form.getEmail())
                .role(role)
                .build();

        storemanagerRepository.save(storemanager);

        if (accountType == AccountType.KAKAO) {
            String kakaoId = oAuth2Provider.getKakaoId(form.getCode());
            Kakao kakao = new Kakao(kakaoId, storemanager);
            storemanager.setKakao(kakao);
            kakaoRepository.save(kakao);
        } else if (accountType == AccountType.NAVER) {
            String naverId = oAuth2Provider.getKakaoId(form.getCode());
            Naver naver = new Naver(naverId, storemanager);
            storemanager.setNaver(naver);
            naverRepository.save(naver);
        } else if (accountType == AccountType.GOOGLE) {
            String googleId = oAuth2Provider.getKakaoId(form.getCode());
            Google google = new Google(googleId, storemanager);
            storemanager.setGoogle(google);
            googleRepository.save(google);
        } else if (accountType == AccountType.PASSWORD) {
            Password password = new Password(passwordEncoder.encode(form.getPassword()), storemanager);
            storemanager.setPassword(password);
            passwordRepository.save(password);
        }


    }

    private Role convertStoremanagerRole(String role) {
        if (Objects.equals(role, "storemanager")) return Role.ROLE_STOREMANAGER;
        else throw new ApiException(ExceptionEnum.INCORRECT_ROLE);
    }

    private AccountType convertAccountType(String accountType) {
        if (Objects.equals(accountType, "kakao")) return AccountType.KAKAO;
        else if (Objects.equals(accountType, "naver")) return AccountType.NAVER;
        else if (Objects.equals(accountType, "google")) return AccountType.GOOGLE;
        else if (Objects.equals(accountType, "password")) return AccountType.PASSWORD;
        else throw new ApiException(ExceptionEnum.INCORRECT_ACCOUNT_TYPE);
    }

}
