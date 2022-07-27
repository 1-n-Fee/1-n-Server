package konkuk.nServer.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * {
 * "code" : "400",
 * "message" : "잘못된 요청입니다.",
 * "validation" :{
 * "title": "값을 입력해주세요."
 * }
 * }
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_EMPTY) // 비어있는 객체는 json으로 안보냄
// 호돌맨의 경우, 비어있는 객체도 그만의 의미가 있다고 생각하기 때문에, 그냥 포함시킴
@RequiredArgsConstructor
public class ApiExceptionResponse {
    private final String errorCode;
    private final String message;
    private final Map<String, String> validation = new HashMap<>();


    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}
