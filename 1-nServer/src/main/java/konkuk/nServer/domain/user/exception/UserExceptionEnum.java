package konkuk.nServer.domain.user.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserExceptionEnum {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "0001", "예상치 못한 서버 에러입니다."),

    INCORRECT_ROLE(HttpStatus.BAD_REQUEST, "M001", "올바르지 않은 Role입니다."),
    INCORRECT_ACCOUNT_TYPE(HttpStatus.BAD_REQUEST, "M002", "올바르지 않은 accountType입니다."),
    INCORRECT_SEX_TYPE(HttpStatus.BAD_REQUEST, "M003", "올바르지 않은 sexType입니다."),
    INCORRECT_PASSWORD(HttpStatus.BAD_REQUEST, "M003", "올바르지 않은 password입니다."),

    NO_FIND_MEMBER(HttpStatus.BAD_REQUEST, "M004", "존재하지 않는 회원ID입니다."),

    ;
    private final HttpStatus status;
    private final String code;
    private String message;

    UserExceptionEnum(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
