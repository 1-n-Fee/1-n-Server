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
import konkuk.nServer.domain.storemanager.repository.StoremanagerFindDao;
import konkuk.nServer.domain.storemanager.repository.StoremanagerRepository;
import konkuk.nServer.domain.user.domain.Role;
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
    private final StoremanagerFindDao storemanagerFindDao;
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
        AccountType accountType = convertProvider.convertAccountType(form.getAccountType());
        Storemanager storemanager = form.toEntity(role, accountType);
        storemanagerRepository.save(storemanager);

        if (accountType == AccountType.PASSWORD) {
            saveOauth(storemanager, accountType, form.getPassword());
        } else {
            String oauthId = oAuth2Provider.getOauthId(accountType, form.getCode());
            saveOauth(storemanager, accountType, oauthId);
        }
    }

    public void signupForApp(StoremanagerSignupForApp form) {
        Role role = convertProvider.convertStoremanagerRole(form.getRole());
        AccountType accountType = convertProvider.convertAccountType(form.getAccountType());
        Storemanager storemanager = form.toEntity(role, accountType);
        storemanagerRepository.save(storemanager);

        if (accountType == AccountType.PASSWORD) {
            saveOauth(storemanager, accountType, form.getPassword());
        } else {
            saveOauth(storemanager, accountType, form.getOauthId());
        }
    }

    private void saveOauth(Storemanager storemanager, AccountType accountType, String id) {
        if (accountType == AccountType.KAKAO) {
            Kakao kakao = new Kakao(id, storemanager);
            storemanager.setKakao(kakao);
            kakaoRepository.save(kakao);
        } else if (accountType == AccountType.NAVER) {
            Naver naver = new Naver(id, storemanager);
            storemanager.setNaver(naver);
            naverRepository.save(naver);
        } else if (accountType == AccountType.GOOGLE) {
            Google google = new Google(id, storemanager);
            storemanager.setGoogle(google);
            googleRepository.save(google);
        } else if (accountType == AccountType.PASSWORD) {
            validatePassword(id);
            Password password = new Password(passwordEncoder.encode(id), storemanager);
            storemanager.setPassword(password);
            passwordRepository.save(password);
        }
    }

    @Transactional(readOnly = true)
    public String oAuthLogin(String oauth, String code) {
        AccountType accountType = convertProvider.convertAccountType(oauth);
        String oauthId = oAuth2Provider.getOauthId(accountType, code);
        Storemanager storemanager = storemanagerFindDao.findStoremanagerByOauth(accountType, oauthId);

        String jwtToken = tokenProvider.createJwt(storemanager.getId(), storemanager.getRole());
        log.info("Token = {}", jwtToken);

        return jwtToken;
    }

    @Transactional(readOnly = true)
    public String oAuthLoginForApp(String oauth, String oauthId) {
        AccountType accountType = convertProvider.convertAccountType(oauth);
        Storemanager storemanager = storemanagerFindDao.findStoremanagerByOauth(accountType, oauthId);

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
    public UserInfo findInfoByStoremanagerId(Long storemanagerId) {
        Storemanager storemanager = storemanagerFindDao.findById(storemanagerId);
        return UserInfo.of(storemanager);
    }
}
