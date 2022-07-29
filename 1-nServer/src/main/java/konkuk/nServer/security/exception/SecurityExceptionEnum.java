package konkuk.nServer.security.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SecurityExceptionEnum {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "0001", "예상치 못한 서버 에러입니다."),

    INCORRECT_LOGIN(HttpStatus.BAD_REQUEST, "S001", "잘못된 로그인 요청입니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "S002", "회원을 찾을 수 없습니다."),
    ;
    private final HttpStatus status;
    private final String code;
    private String message;

    SecurityExceptionEnum(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
