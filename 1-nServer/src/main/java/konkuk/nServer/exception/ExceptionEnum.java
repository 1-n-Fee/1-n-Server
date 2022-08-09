package konkuk.nServer.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionEnum {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "0001", "예상치 못한 서버 에러입니다."),

    INCORRECT_ROLE(HttpStatus.BAD_REQUEST, "M001", "올바르지 않은 Role입니다."),
    INCORRECT_ACCOUNT_TYPE(HttpStatus.BAD_REQUEST, "M002", "올바르지 않은 accountType입니다."),
    INCORRECT_SEX_TYPE(HttpStatus.BAD_REQUEST, "M003", "올바르지 않은 sexType입니다."),
    INCORRECT_PASSWORD(HttpStatus.BAD_REQUEST, "M004", "올바르지 않은 password입니다."),

    INCORRECT_NICKNAME(HttpStatus.BAD_REQUEST, "M005", "올바르지 않은 nickname입니다."),
    NO_FOUND_USER(HttpStatus.BAD_REQUEST, "M006", "회원을 찾을 수 없습니다."),

    INCORRECT_LOGIN(HttpStatus.BAD_REQUEST, "L001", "잘못된 로그인 요청입니다."),

    INCORRECT_SPOT(HttpStatus.BAD_REQUEST, "P001", "올바르지 않은 spot입니다."),
    INCORRECT_CATEGORY(HttpStatus.BAD_REQUEST, "P002", "올바르지 않은 category입니다."),
    NO_FOUND_STORE(HttpStatus.BAD_REQUEST, "P003", "store를 찾을 수 없습니다."),
    NO_FOUND_POST(HttpStatus.BAD_REQUEST, "P004", "post를 찾을 수 없습니다."),
    NO_FOUND_MENU(HttpStatus.BAD_REQUEST, "P005", "menu를 찾을 수 없습니다."),
    NOT_MATCH_OWNER(HttpStatus.BAD_REQUEST, "P006", "해당 회원은 post owner이 아닙니다."),
    NO_FOUND_PROPOSAL_DETAIL(HttpStatus.BAD_REQUEST, "P007", "proposalDetail을 찾을 수 없습니다."),
    NO_FOUND_PROPOSAL(HttpStatus.BAD_REQUEST, "P008", "proposal을 찾을 수 없습니다."),
    NO_CHANGE_PROPOSAL(HttpStatus.BAD_REQUEST, "P009", "이미 승인 또는 거절된 제안서입니다."),

    FAIL_STORE_IMAGE(HttpStatus.BAD_REQUEST, "I001", "이미지 저장에 실패했습니다."),
    FAIL_CALL_IMAGE(HttpStatus.BAD_REQUEST, "I002", "이미지를 불러오는데 실패했습니다."),


    INCORRECT_HOUR(HttpStatus.BAD_REQUEST, "S001", "잘못된 hour입니다. (hhmm-hhmm 필요)"),

    ;
    private final HttpStatus status;
    private final String code;
    private final String message;

    ExceptionEnum(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
