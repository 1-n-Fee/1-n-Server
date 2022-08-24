package konkuk.nServer.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionEnum {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "0001", "예상치 못한 서버 에러입니다."),

    INCORRECT_ROLE(HttpStatus.BAD_REQUEST, "U001", "올바르지 않은 Role입니다."),
    INCORRECT_ACCOUNT_TYPE(HttpStatus.BAD_REQUEST, "U002", "올바르지 않은 accountType입니다."),
    INCORRECT_SEX_TYPE(HttpStatus.BAD_REQUEST, "U003", "올바르지 않은 sexType입니다."),
    INCORRECT_PASSWORD(HttpStatus.BAD_REQUEST, "U004", "올바르지 않은 password입니다."),

    INCORRECT_NICKNAME(HttpStatus.BAD_REQUEST, "U005", "올바르지 않은 nickname입니다."),
    NO_FOUND_USER(HttpStatus.BAD_REQUEST, "U006", "회원을 찾을 수 없습니다."),
    NO_FOUND_STOREMANAGER(HttpStatus.BAD_REQUEST, "U007", "storemanager을 찾을 수 없습니다."),

    INCORRECT_LOGIN(HttpStatus.BAD_REQUEST, "L001", "잘못된 로그인 요청입니다."),
    FAIL_LOGIN(HttpStatus.BAD_REQUEST, "L002", "로그인에 실패했습니다."),

    INCORRECT_SPOT(HttpStatus.BAD_REQUEST, "P001", "올바르지 않은 spot입니다."),
    INCORRECT_CATEGORY(HttpStatus.BAD_REQUEST, "P002", "올바르지 않은 category입니다."),
    NO_FOUND_STORE(HttpStatus.BAD_REQUEST, "P003", "store를 찾을 수 없습니다."),
    NO_FOUND_POST(HttpStatus.BAD_REQUEST, "P004", "post를 찾을 수 없습니다."),
    NO_FOUND_MENU(HttpStatus.BAD_REQUEST, "P005", "menu를 찾을 수 없습니다."),
    NOT_OWNER_POST(HttpStatus.BAD_REQUEST, "P006", "해당 회원은 post owner이 아닙니다."),
    NO_FOUND_PROPOSAL_DETAIL(HttpStatus.BAD_REQUEST, "P007", "proposalDetail을 찾을 수 없습니다."),
    NO_FOUND_PROPOSAL(HttpStatus.BAD_REQUEST, "P008", "proposal을 찾을 수 없습니다."),
    NO_CHANGE_PROPOSAL(HttpStatus.BAD_REQUEST, "P009", "이미 승인 또는 거절된 제안서입니다."),
    NOT_DELETE_POST(HttpStatus.BAD_REQUEST, "P010", "해당 post는 삭제할 수 없는 단계입니다."),
    NOT_OWNER_PROPOSAL(HttpStatus.BAD_REQUEST, "P011", "해당 회원은 proposal owner이 아닙니다."),
    OWNER_POST_PROPOSAL(HttpStatus.BAD_REQUEST, "P012", "본인의 post는 제안서를 보낼 수 없습니다."),
    NO_FOUND_COMMENT(HttpStatus.BAD_REQUEST, "P013", "해당 댓글을 찾을 수 없습니다"),
    NOT_ACCESS_POST(HttpStatus.BAD_REQUEST, "P014", "해당 post에는 접근할 수 없습니다."),
    INCORRECT_POST_PROCESS(HttpStatus.BAD_REQUEST, "P015", "올바르지 않는 postProcess입니다."),

    FAIL_STORE_IMAGE(HttpStatus.BAD_REQUEST, "I001", "이미지 저장에 실패했습니다."),
    FAIL_CALL_IMAGE(HttpStatus.BAD_REQUEST, "I002", "이미지를 불러오는데 실패했습니다."),

    INCORRECT_HOUR(HttpStatus.BAD_REQUEST, "S001", "잘못된 hour입니다. (hhmm-hhmm 필요)"),

    INCORRECT_MESSAGE_TYPE(HttpStatus.BAD_REQUEST, "M001", "잘못된 메시지 타입입니다."),
    NOT_LOGIN_CHAT(HttpStatus.BAD_REQUEST, "M002", "채팅은 로그인 회원만 가능합니다."),

    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "T001", "유효기간이 지난 토큰입니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "T002", "토큰 검증에 실패했습니다."),
    NOT_FOUND_TOKEN(HttpStatus.BAD_REQUEST, "T003", "토큰을 찾을 수 없습니다."),
    ACCESS_DENIED(HttpStatus.BAD_REQUEST, "T004", "권한이 없습니다."),

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
