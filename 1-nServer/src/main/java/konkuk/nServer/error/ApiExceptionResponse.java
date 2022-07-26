package konkuk.nServer.error;

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
@RequiredArgsConstructor
public class ApiExceptionResponse {
    private final String errorCode;
    private final String message;
}