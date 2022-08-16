package konkuk.nServer.domain.user.service;

import konkuk.nServer.domain.account.domain.*;
import konkuk.nServer.domain.account.repository.GoogleRepository;
import konkuk.nServer.domain.account.repository.KakaoRepository;
import konkuk.nServer.domain.account.repository.NaverRepository;
import konkuk.nServer.domain.account.repository.PasswordRepository;
import konkuk.nServer.domain.account.service.OAuth2Provider;
import konkuk.nServer.domain.common.service.ConvertProvider;
import konkuk.nServer.domain.user.domain.*;
import konkuk.nServer.domain.user.dto.requestForm.UserSignup;
import konkuk.nServer.domain.user.dto.requestForm.UserSignupForApp;
import konkuk.nServer.domain.user.dto.responseForm.UserInfo;
import konkuk.nServer.domain.user.repository.*;
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
public class UserService {
    private final UserRepository userRepository;
    private final KakaoRepository kakaoRepository;
    private final NaverRepository naverRepository;
    private final GoogleRepository googleRepository;
    private final PasswordRepository passwordRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final OAuth2Provider oAuth2Provider;
    private final JwtTokenProvider tokenProvider;

    private final ConvertProvider convertProvider;

    public void signup(UserSignup form) {
        Role role = convertProvider.convertStudentRole(form.getRole());
        if (role != Role.ROLE_STUDENT)
            throw new ApiException(ExceptionEnum.INCORRECT_ROLE);

        AccountType accountType = convertProvider.convertAccountType(form.getAccountType());
        SexType sexType = convertProvider.convertSexType(form.getSexType());

        User user = form.toEntity(role, accountType, sexType);
        userRepository.save(user);

        if (accountType == AccountType.KAKAO) {
            String kakaoId = oAuth2Provider.getKakaoId(form.getCode());
            Kakao kakao = new Kakao(kakaoId, user);
            user.setKakao(kakao);
            kakaoRepository.save(kakao);
        } else if (accountType == AccountType.NAVER) {
            String naverId = oAuth2Provider.getNaverId(form.getCode());
            Naver naver = new Naver(naverId, user);
            user.setNaver(naver);
            naverRepository.save(naver);
        } else if (accountType == AccountType.GOOGLE) {
            String googleId = oAuth2Provider.getGoogleId(form.getCode());
            Google google = new Google(googleId, user);
            user.setGoogle(google);
            googleRepository.save(google);
        } else if (accountType == AccountType.PASSWORD) {
            validatePassword(form.getPassword());
            Password password = new Password(passwordEncoder.encode(form.getPassword()), user);
            user.setPassword(password);
            passwordRepository.save(password);
        }
    }

    public void signupForApp(UserSignupForApp form) {
        Role role = convertProvider.convertStudentRole(form.getRole());
        if (role != Role.ROLE_STUDENT)
            throw new ApiException(ExceptionEnum.INCORRECT_ROLE);

        AccountType accountType = convertProvider.convertAccountType(form.getAccountType());
        SexType sexType = convertProvider.convertSexType(form.getSexType());

        User user = form.toEntity(role, accountType, sexType);
        userRepository.save(user);

        if (accountType == AccountType.KAKAO) {
            Kakao kakao = new Kakao(form.getOauthId(), user);
            user.setKakao(kakao);
            kakaoRepository.save(kakao);
        } else if (accountType == AccountType.NAVER) {
            Naver naver = new Naver(form.getOauthId(), user);
            user.setNaver(naver);
            naverRepository.save(naver);
        } else if (accountType == AccountType.GOOGLE) {
            Google google = new Google(form.getOauthId(), user);
            user.setGoogle(google);
            googleRepository.save(google);
        } else if (accountType == AccountType.PASSWORD) {
            validatePassword(form.getPassword());
            Password password = new Password(passwordEncoder.encode(form.getPassword()), user);
            user.setPassword(password);
            passwordRepository.save(password);
        }
    }

    public void changePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));

        if (user.getAccountType() != AccountType.PASSWORD)
            throw new ApiException(ExceptionEnum.INCORRECT_ACCOUNT_TYPE);

        if (passwordEncoder.matches(user.getPassword().getPassword(), newPassword))
            throw new ApiException(ExceptionEnum.INCORRECT_PASSWORD);

        user.getPassword().changePassword(passwordEncoder.encode(newPassword));
    }

    public void changeNickname(Long userId, String nickname) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));

        if (userRepository.existsByNickname(nickname))
            throw new ApiException(ExceptionEnum.INCORRECT_NICKNAME);

        user.changeNickname(nickname);
    }

    public void changeSexType(Long userId, String sexType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));

        user.changeSexType(convertProvider.convertSexType(sexType));
    }

    @Transactional(readOnly = true)
    public boolean isDuplicateNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Transactional(readOnly = true)
    public String oAuthLogin(String oauth, String code) {
        AccountType accountType = convertProvider.convertAccountType(oauth);
        User user = null;
        if (accountType == AccountType.KAKAO) {
            String kakaoId = oAuth2Provider.getKakaoId(code);
            Kakao kakao = kakaoRepository.findByKakaoId(kakaoId)
                    .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));
            user = kakao.getUser();
        } else if (accountType == AccountType.NAVER) {
            String naverId = oAuth2Provider.getNaverId(code);
            Naver naver = naverRepository.findByNaverId(naverId)
                    .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));
            user = naver.getUser();
        } else if (accountType == AccountType.GOOGLE) {
            String googleId = oAuth2Provider.getGoogleId(code);
            Google google = googleRepository.findByGoogleId(googleId)
                    .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));
            user = google.getUser();
        } else throw new ApiException(ExceptionEnum.NO_FOUND_USER);

        String jwtToken = tokenProvider.createJwt(user);
        log.info("Token = {}", jwtToken);

        return jwtToken;
    }

    @Transactional(readOnly = true)
    public String oAuthLoginForApp(String oauth, String oauthId) {
        AccountType accountType = convertProvider.convertAccountType(oauth);
        User user = null;
        if (accountType == AccountType.KAKAO) {
            Kakao kakao = kakaoRepository.findByKakaoId(oauthId)
                    .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));
            user = kakao.getUser();
        } else if (accountType == AccountType.NAVER) {
            Naver naver = naverRepository.findByNaverId(oauthId)
                    .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));
            user = naver.getUser();
        } else if (accountType == AccountType.GOOGLE) {
            Google google = googleRepository.findByGoogleId(oauthId)
                    .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));
            user = google.getUser();
        } else throw new ApiException(ExceptionEnum.NO_FOUND_USER);

        String jwtToken = tokenProvider.createJwt(user);
        log.info("Token = {}", jwtToken);

        return jwtToken;
    }

    public String findPassword(String email, String name, String phone) {
        User user = userRepository.findUserForPassword(email, name, phone)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));

        if (user.getAccountType() != AccountType.PASSWORD)
            throw new ApiException(ExceptionEnum.INCORRECT_ACCOUNT_TYPE);

        String newPassword = randomPw();
        user.getPassword().changePassword(passwordEncoder.encode(newPassword));

        return newPassword;
    }


    @Transactional(readOnly = true)
    public String findEmail(String name, String phone) {
        User user = userRepository.findByNameAndPhone(name, phone)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));
        return user.getEmail();
    }

    @Transactional(readOnly = true)
    public UserInfo findInfoByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));
        return UserInfo.of(user);
    }

    private void validatePassword(String password) {
        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@!%*#?&])[A-Za-z\\d$@!%*#?&]{8,15}$")) {
            throw new ApiException(ExceptionEnum.INCORRECT_PASSWORD);
        }
    }

    private String randomPw() {
        char[] pwCollectionSpCha = new char[]{'!', '@', '#', '$', '%', '^', '&', '*', '(', ')'};
        char[] pwCollectionNum = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',};
        char[] pwCollectionAll = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '!', '@', '#', '$', '%', '^', '&', '*', '(', ')'};
        return getRandPw(1, pwCollectionSpCha) + getRandPw(8, pwCollectionAll) + getRandPw(1, pwCollectionNum);
    }

    private String getRandPw(int size, char[] pwCollection) {
        StringBuilder ranPw = new StringBuilder();
        for (int i = 0; i < size; i++) {
            int selectRandomPw = (int) (Math.random() * (pwCollection.length));
            ranPw.append(pwCollection[selectRandomPw]);
        }
        return ranPw.toString();
    }
}
