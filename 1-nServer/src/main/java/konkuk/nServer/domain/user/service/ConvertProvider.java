package konkuk.nServer.domain.user.service;

import konkuk.nServer.domain.user.domain.AccountType;
import konkuk.nServer.domain.user.domain.Role;
import konkuk.nServer.domain.user.domain.SexType;
import konkuk.nServer.exception.ApiException;
import konkuk.nServer.exception.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConvertProvider {


    public SexType convertSexType(String sexType) {
        if (Objects.equals(sexType, "man")) return SexType.MAN;
        else if (Objects.equals(sexType, "woman")) return SexType.WOMAN;
        else if (sexType == null) return null;
        else throw new ApiException(ExceptionEnum.INCORRECT_SEX_TYPE);
    }

    public Role convertStudentRole(String role) {
        if (Objects.equals(role, "student")) return Role.ROLE_STUDENT;
        else throw new ApiException(ExceptionEnum.INCORRECT_ROLE);
    }

    public Role convertStoremanagerRole(String role) {
        if (Objects.equals(role, "storemanager")) return Role.ROLE_STOREMANAGER;
        else throw new ApiException(ExceptionEnum.INCORRECT_ROLE);
    }

    public AccountType convertAccountType(String accountType) {
        if (Objects.equals(accountType, "kakao")) return AccountType.KAKAO;
        else if (Objects.equals(accountType, "naver")) return AccountType.NAVER;
        else if (Objects.equals(accountType, "google")) return AccountType.GOOGLE;
        else if (Objects.equals(accountType, "password")) return AccountType.PASSWORD;
        else throw new ApiException(ExceptionEnum.INCORRECT_ACCOUNT_TYPE);
    }
}
