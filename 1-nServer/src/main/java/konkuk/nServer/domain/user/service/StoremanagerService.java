package konkuk.nServer.domain.user.service;

import konkuk.nServer.domain.common.service.ConvertProvider;
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
    private final ConvertProvider convertProvider;

    public void signup(StoremanagerSignup form) {
        Role role = convertProvider.convertStoremanagerRole(form.getRole());
        if (role != Role.ROLE_STOREMANAGER) {
            throw new ApiException(ExceptionEnum.INCORRECT_ROLE);
        }
        AccountType accountType = convertProvider.convertAccountType(form.getAccountType());

        Storemanager storemanager = form.toEntity(role, accountType);

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
}
