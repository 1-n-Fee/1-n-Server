package konkuk.nServer.domain.common.service;

import konkuk.nServer.domain.post.domain.Category;
import konkuk.nServer.domain.post.domain.Spot;
import konkuk.nServer.domain.user.domain.AccountType;
import konkuk.nServer.domain.user.domain.Role;
import konkuk.nServer.domain.user.domain.SexType;
import konkuk.nServer.exception.ApiException;
import konkuk.nServer.exception.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public Category convertCategory(String category) {
        //KOREAN, CHINESE, WESTERN, JAPANESE, MIDNIGHT, ETC
        if (Objects.equals(category, "korean")) return Category.KOREAN;
        else if (Objects.equals(category, "chinese")) return Category.CHINESE;
        else if (Objects.equals(category, "western")) return Category.WESTERN;
        else if (Objects.equals(category, "japanese")) return Category.JAPANESE;
        else if (Objects.equals(category, "midnight")) return Category.MIDNIGHT;
        else if (Objects.equals(category, "etc")) return Category.ETC;
        else throw new ApiException(ExceptionEnum.INCORRECT_CATEGORY);
    }

    public Spot convertSpot(Long spotId) {
        for (Spot spot : Spot.values()) {
            if (spot.getId() == spotId) return spot;
        }
        throw new ApiException(ExceptionEnum.INCORRECT_SPOT);
    }

    public LocalDateTime convertTime(String dateTime) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy.MM.dd.HH.mm"));
    }

    public Spot convertSpotById(Long spotId) {
        Spot[] values = Spot.values();
        for (Spot spot : values) {
            if (spot.getId() == spotId) return spot;
        }
        throw new ApiException(ExceptionEnum.INCORRECT_SPOT);
    }

}
