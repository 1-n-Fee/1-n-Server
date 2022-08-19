package konkuk.nServer.domain.storemanager.service;

import konkuk.nServer.domain.account.domain.*;
import konkuk.nServer.domain.account.repository.GoogleRepository;
import konkuk.nServer.domain.account.repository.KakaoRepository;
import konkuk.nServer.domain.account.repository.NaverRepository;
import konkuk.nServer.domain.account.repository.PasswordRepository;
import konkuk.nServer.domain.account.service.OAuth2Provider;
import konkuk.nServer.domain.common.service.ConvertProvider;
import konkuk.nServer.domain.storemanager.domain.Storemanager;
import konkuk.nServer.domain.storemanager.dto.request.StoremanagerSignup;
import konkuk.nServer.domain.storemanager.dto.request.StoremanagerSignupForApp;
import konkuk.nServer.domain.storemanager.repository.StoremanagerRepository;
import konkuk.nServer.domain.user.domain.Role;
import konkuk.nServer.domain.user.domain.User;
import konkuk.nServer.domain.user.dto.responseForm.UserInfo;
import konkuk.nServer.exception.ApiException;
import konkuk.nServer.exception.ExceptionEnum;
import konkuk.nServer.security.jwt.JwtTokenProvider;
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
    private final JwtTokenProvider tokenProvider;

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
            validatePassword(form.getPassword());
            Password password = new Password(passwordEncoder.encode(form.getPassword()), storemanager);
            storemanager.setPassword(password);
            passwordRepository.save(password);
        }
    }

    public void signupForApp(StoremanagerSignupForApp form) {
        Role role = convertProvider.convertStoremanagerRole(form.getRole());
        if (role != Role.ROLE_STOREMANAGER) {
            throw new ApiException(ExceptionEnum.INCORRECT_ROLE);
        }
        AccountType accountType = convertProvider.convertAccountType(form.getAccountType());

        Storemanager storemanager = form.toEntity(role, accountType);

        storemanagerRepository.save(storemanager);

        if (accountType == AccountType.KAKAO) {
            Kakao kakao = new Kakao(form.getOauthId(), storemanager);
            storemanager.setKakao(kakao);
            kakaoRepository.save(kakao);
        } else if (accountType == AccountType.NAVER) {
            Naver naver = new Naver(form.getOauthId(), storemanager);
            storemanager.setNaver(naver);
            naverRepository.save(naver);
        } else if (accountType == AccountType.GOOGLE) {
            Google google = new Google(form.getOauthId(), storemanager);
            storemanager.setGoogle(google);
            googleRepository.save(google);
        } else if (accountType == AccountType.PASSWORD) {
            validatePassword(form.getPassword());
            Password password = new Password(passwordEncoder.encode(form.getPassword()), storemanager);
            storemanager.setPassword(password);
            passwordRepository.save(password);
        }
    }

    @Transactional(readOnly = true)
    public String oAuthLogin(String oauth, String code) {
        AccountType accountType = convertProvider.convertAccountType(oauth);
        Storemanager storemanager = null;
        if (accountType == AccountType.KAKAO) {
            String kakaoId = oAuth2Provider.getKakaoId(code);
            Kakao kakao = kakaoRepository.findByKakaoId(kakaoId)
                    .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));
            storemanager = kakao.getStoremanager();
        } else if (accountType == AccountType.NAVER) {
            String naverId = oAuth2Provider.getNaverId(code);
            Naver naver = naverRepository.findByNaverId(naverId)
                    .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));
            storemanager = naver.getStoremanager();
        } else if (accountType == AccountType.GOOGLE) {
            String googleId = oAuth2Provider.getGoogleId(code);
            Google google = googleRepository.findByGoogleId(googleId)
                    .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));
            storemanager = google.getStoremanager();
        } else throw new ApiException(ExceptionEnum.NO_FOUND_USER);

        String jwtToken = tokenProvider.createJwt(storemanager.getId(), storemanager.getRole());
        log.info("Token = {}", jwtToken);

        return jwtToken;
    }

    @Transactional(readOnly = true)
    public String oAuthLoginForApp(String oauth, String oauthId) {
        AccountType accountType = convertProvider.convertAccountType(oauth);
        Storemanager storemanager = null;
        if (accountType == AccountType.KAKAO) {
            Kakao kakao = kakaoRepository.findByKakaoId(oauthId)
                    .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_STOREMANAGER));
            storemanager = kakao.getStoremanager();
        } else if (accountType == AccountType.NAVER) {
            Naver naver = naverRepository.findByNaverId(oauthId)
                    .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_STOREMANAGER));
            storemanager = naver.getStoremanager();
        } else if (accountType == AccountType.GOOGLE) {
            Google google = googleRepository.findByGoogleId(oauthId)
                    .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_STOREMANAGER));
            storemanager = google.getStoremanager();
        } else throw new ApiException(ExceptionEnum.NO_FOUND_STOREMANAGER);

        String jwtToken = tokenProvider.createJwt(storemanager.getId(), storemanager.getRole());
        log.info("Token = {}", jwtToken);

        return jwtToken;
    }


    private void validatePassword(String password) {
        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@!%*#?&])[A-Za-z\\d$@!%*#?&]{8,15}$")) {
            throw new ApiException(ExceptionEnum.INCORRECT_PASSWORD);
        }
    }

    @Transactional(readOnly = true)
    public UserInfo findInfoByUserId(Long storemanagerId) {
        Storemanager storemanager = storemanagerRepository.findById(storemanagerId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));
        return UserInfo.of(storemanager);
    }
}
