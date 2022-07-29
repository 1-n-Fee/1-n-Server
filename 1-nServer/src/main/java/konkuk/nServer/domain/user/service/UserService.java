package konkuk.nServer.domain.user.service;

import konkuk.nServer.domain.user.domain.*;
import konkuk.nServer.domain.user.dto.requestForm.RequestUserSignup;
import konkuk.nServer.domain.user.exception.UserExceptionEnum;
import konkuk.nServer.domain.user.repository.*;
import konkuk.nServer.exception.ApiException;
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


    public void signup(RequestUserSignup form) {
        Role role = convertRole(form.getRole());
        AccountType accountType = convertAccountType(form.getAccountType());

        if (role != Role.ROLE_STUDENT) throw new ApiException(UserExceptionEnum.INCORRECT_ROLE);
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
            Kakao kakao = new Kakao(form.getKakaoId(), user);
            user.setKakao(kakao);
            kakaoRepository.save(kakao);
        } else if (accountType == AccountType.NAVER) {
            Naver naver = new Naver(form.getNaverId(), user);
            user.setNaver(naver);
            naverRepository.save(naver);
        } else if (accountType == AccountType.GOOGLE) {
            Google google = new Google(form.getGoogleId(), user);
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
                .orElseThrow(() -> new ApiException(UserExceptionEnum.NO_FIND_MEMBER));

        if (user.getAccountType() != AccountType.PASSWORD)
            throw new ApiException(UserExceptionEnum.INCORRECT_ACCOUNT_TYPE);

        user.getPassword().changePassword(newPassword);
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

    private SexType convertSexType(String sexType) {
        if (Objects.equals(sexType, "man")) return SexType.MAN;
        else if (Objects.equals(sexType, "woman")) return SexType.WOMAN;
        else if (sexType == null) return null;
        else throw new ApiException(UserExceptionEnum.INCORRECT_SEX_TYPE);
    }

    private Role convertRole(String role) {
        if (Objects.equals(role, "student")) return Role.ROLE_STUDENT;
        else if (Objects.equals(role, "storemanager")) return Role.ROLE_STOREMANAGER;
        else throw new ApiException(UserExceptionEnum.INCORRECT_ROLE);
    }

    private AccountType convertAccountType(String accountType) {
        if (Objects.equals(accountType, "kakao")) return AccountType.KAKAO;
        else if (Objects.equals(accountType, "naver")) return AccountType.NAVER;
        else if (Objects.equals(accountType, "google")) return AccountType.GOOGLE;
        else if (Objects.equals(accountType, "password")) return AccountType.PASSWORD;
        else throw new ApiException(UserExceptionEnum.INCORRECT_ACCOUNT_TYPE);
    }

    public boolean isDuplicateNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}

