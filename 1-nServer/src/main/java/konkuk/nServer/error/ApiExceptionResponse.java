package konkuk.nServer.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 회사마다, 조직마다 반환 형식이 다름
 */
@Data
@RequiredArgsConstructor
public class ApiExceptionResponse {
    private final String errorCode;
    private final String message;
}
