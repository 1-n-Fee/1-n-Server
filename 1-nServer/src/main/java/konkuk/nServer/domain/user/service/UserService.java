package konkuk.nServer.domain.user.service;

import konkuk.nServer.domain.user.domain.*;
import konkuk.nServer.domain.user.dto.requestForm.UserSignup;
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

import java.util.Objects;

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


    public void signup(UserSignup form) {
        Role role = convertStudentRole(form.getRole());
        AccountType accountType = convertAccountType(form.getAccountType());

        if (role != Role.ROLE_STUDENT) throw new ApiException(ExceptionEnum.INCORRECT_ROLE);

        User user = User.builder()
                .accountType(accountType)
                .name(form.getName())
                .phone(form.getPhone())
                .role(role)
                .nickname(form.getNickname())
                .email(form.getEmail())
                .major(form.getMajor())
                .sexType(convertSexType(form.getSexType()))
                .build();
        userRepository.save(user);

        if (accountType == AccountType.KAKAO) {
            String kakaoId = oAuth2Provider.getKakaoId(form.getCode());
            Kakao kakao = new Kakao(kakaoId, user);
            user.setKakao(kakao);
            kakaoRepository.save(kakao);
        } else if (accountType == AccountType.NAVER) {
            String naverId = oAuth2Provider.getKakaoId(form.getCode());
            Naver naver = new Naver(naverId, user);
            user.setNaver(naver);
            naverRepository.save(naver);
        } else if (accountType == AccountType.GOOGLE) {
            String googleId = oAuth2Provider.getKakaoId(form.getCode());
            Google google = new Google(googleId, user);
            user.setGoogle(google);
            googleRepository.save(google);
        } else if (accountType == AccountType.PASSWORD) {
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

        user.changeSexType(convertSexType(sexType));
    }

    private SexType convertSexType(String sexType) {
        if (Objects.equals(sexType, "man")) return SexType.MAN;
        else if (Objects.equals(sexType, "woman")) return SexType.WOMAN;
        else if (sexType == null) return null;
        else throw new ApiException(ExceptionEnum.INCORRECT_SEX_TYPE);
    }

    private Role convertStudentRole(String role) {
        if (Objects.equals(role, "student")) return Role.ROLE_STUDENT;
        else throw new ApiException(ExceptionEnum.INCORRECT_ROLE);
    }

    private AccountType convertAccountType(String accountType) {
        if (Objects.equals(accountType, "kakao")) return AccountType.KAKAO;
        else if (Objects.equals(accountType, "naver")) return AccountType.NAVER;
        else if (Objects.equals(accountType, "google")) return AccountType.GOOGLE;
        else if (Objects.equals(accountType, "password")) return AccountType.PASSWORD;
        else throw new ApiException(ExceptionEnum.INCORRECT_ACCOUNT_TYPE);
    }

    @Transactional(readOnly = true)
    public boolean isDuplicateNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public String oAuthLogin(String oauth, String code) {
        AccountType accountType = convertAccountType(oauth);
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

    public String findPassword(String email, String name, String phone) {
        User user = userRepository.findUserForPassword(email, name, phone)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));

        if (user.getAccountType() != AccountType.PASSWORD)
            throw new ApiException(ExceptionEnum.INCORRECT_ACCOUNT_TYPE);

        String newPassword = randomPw();
        user.getPassword().changePassword(passwordEncoder.encode(newPassword));

        return newPassword;
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

    public String findEmail(String name, String phone) {
        User user = userRepository.findByNameAndPhone(name, phone)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));
        return user.getEmail();
    }

    public UserInfo findInfoByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.NO_FOUND_USER));

        return UserInfo.builder()
                .accountType(user.getAccountType().toString())
                .phone(user.getPhone())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().toString())
                .nickname(user.getNickname())
                .major(user.getMajor())
                .sexType(user.getSexType() != null ? user.getSexType().toString() : null)
                .build();
    }
}
