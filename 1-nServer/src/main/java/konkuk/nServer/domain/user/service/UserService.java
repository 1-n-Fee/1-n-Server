package konkuk.nServer.domain.user.service;

import konkuk.nServer.domain.user.domain.*;
import konkuk.nServer.domain.user.dto.requestForm.RequestSignupForm;
import konkuk.nServer.domain.user.error.UserExceptionEnum;
import konkuk.nServer.domain.user.repository.*;
import konkuk.nServer.error.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final StoremanagerRepository storemanagerRepository;
    private final KakaoRepository kakaoRepository;
    private final NaverRepository naverRepository;
    private final GoogleRepository googleRepository;
    private final PasswordRepository passwordRepository;

    public void signup(RequestSignupForm form) {
        Role role = convertRole(form.getRole());
        AccountType accountType = convertAccountType(form.getAccountType());

        User user = null;
        if (role == Role.ROLE_STUDENT) {
            user = Student.builder()
                    .accountType(accountType)
                    .name(form.getName())
                    .phone(form.getPhone())
                    .role(role)
                    .nickname(form.getStudent().getNickname())
                    .email(form.getStudent().getEmail())
                    .major(form.getStudent().getMajor())
                    .sexType(convertSexType(form.getStudent().getSexType()))
                    .build();

            studentRepository.save((Student) user);
        } else if (role == Role.ROLE_STOREMANAGER) {
            user = Storemanager.builder()
                    .accountType(accountType)
                    .name(form.getName())
                    .phone(form.getPhone())
                    .role(role)
                    .storeName(form.getStoremanager().getStoreName())
                    .storePhone(form.getStoremanager().getStorePhone())
                    .storeAddress(form.getStoremanager().getStoreAddress())
                    .storeRegistrationNumber(form.getStoremanager().getStoreRegistrationNumber())
                    .build();

            storemanagerRepository.save((Storemanager) user);
        }

        if (accountType == AccountType.KAKAO) {
            Kakao kakao = new Kakao(form.getAccount().getKakaoId(), user);
            user.setKakao(kakao);
            kakaoRepository.save(kakao);
        } else if (accountType == AccountType.NAVER) {
            Naver naver = new Naver(form.getAccount().getNaverId(), user);
            user.setNaver(naver);
            naverRepository.save(naver);
        } else if (accountType == AccountType.GOOGLE) {
            Google google = new Google(form.getAccount().getGoogleId(), user);
            user.setGoogle(google);
            googleRepository.save(google);
        } else if (accountType == AccountType.PASSWORD) {
            Password password = new Password(form.getAccount().getPassword(), user);
            user.setPassword(password);
            passwordRepository.save(password);
        }
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
        return studentRepository.existsByNickname(nickname);
    }
}

